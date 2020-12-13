package com.sekwah.mira4j.network;

import static com.sekwah.mira4j.Mira4J.*;

import java.util.*;

import com.sekwah.mira4j.config.ServerConfig;
import com.sekwah.mira4j.game.GameManager;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class Server implements Runnable {
    private static Server instance;
    private final int port;
    private final String address;
    
    private final GameManager gameManager;

    private final List<ClientConnectionManager> managers = Collections.synchronizedList(new ArrayList<ClientConnectionManager>());
    private final Thread tickThread = new Thread(this);
    
    public static Server getInstance() {
        return instance;
    }

    public Server(GameManager gameManager, ServerConfig serverConfig) {
        instance = this;
        
        this.gameManager = gameManager;
        this.address = serverConfig.address;
        this.port = serverConfig.port;
    }

    public void start() throws Exception {
        final NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            tickThread.setDaemon(true);
            tickThread.start();
            
            final Bootstrap b = new Bootstrap();
            b.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<DatagramChannel>() {
                        @Override
                        public void initChannel(final DatagramChannel ch) throws Exception {
                            ClientConnectionManager manager = new ClientConnectionManager();
                            
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(
                                new IncomingPacketHandler(manager),
                                new OutgoingPacketHandler()
                            );
                            
                            managers.add(manager);
                            manager.setPacketListener(new ClientListener(gameManager, manager));
                            p.addLast(manager);
                        }
                    });
            
            LOGGER.info("waiting for message on {}:{}", address, port);
            b.bind(address, port).sync().channel().closeFuture().await();
        } finally {
            LOGGER.info("Server Closing");
        }
    }
    
    public void tick() {
        synchronized(managers) {
            Iterator<ClientConnectionManager> iterator = managers.iterator();
            while(iterator.hasNext()) {
                ClientConnectionManager manager = iterator.next();
                
                if(manager.hasClient()) {
                    try {
                        manager.tick();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    
                    continue;
                }
                
                iterator.remove();
                manager.disconnect();
            }
        }
    }

    @Override
    public void run() {
        long last = System.currentTimeMillis();
        try {
            while(true) {
                if(Thread.interrupted())
                    break;
                
                long now = System.currentTimeMillis();
                
                // Tell the player that the server is overloaded ???
                if(now + 20L > last) {
                    last += 20L;
                    
                    tick();
                } else {
                    Thread.sleep(1);
                }
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
