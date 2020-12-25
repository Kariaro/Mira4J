package com.sekwah.mira4j.network;

import java.net.InetSocketAddress;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.sekwah.mira4j.impl.unity.GameManager;

import io.netty.channel.*;
import io.netty.channel.socket.DatagramChannel;

/**
 * This class is connected to a client 
 *
 * @author HardCoded
 */
public class ClientConnectionManager extends SimpleChannelInboundHandler<Packet<?>> {
    private final Queue<Packet<?>> packetQueue = new ConcurrentLinkedQueue<>();
    private DatagramChannel channel;
    private PacketListener listener;
    private boolean hasRemote;
    private int sessionId;
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channel = (DatagramChannel)ctx.channel();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet<?> msg) throws Exception {
        forwardToPacket(msg, listener);
    }
    
    @SuppressWarnings("unchecked")
    private <T> void forwardToPacket(Packet<T> packet, PacketListener packetListener) {
        try {
            packet.forwardPacket((T)packetListener);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
    
    public int getSessionId() {
        return sessionId;
    }
    
    public boolean hasClient() {
        return channel != null && (!hasRemote || channel.isConnected()) && channel.isOpen();
    }
    
    public void tick() {
        updateQueue();
        
        if (channel != null) {
            channel.flush(); 
        }
    }
    
    private void sendPacketBack(Packet<?> packet) {
        if (channel.eventLoop().inEventLoop()) {
            ChannelFuture channelfuture = this.channel.writeAndFlush(packet);
            channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        } else {
            channel.eventLoop().execute(() -> {
                ChannelFuture channelfuture = this.channel.writeAndFlush(packet);
                channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            });
        }
    }
    
    public void updateQueue() {
        if (!hasClient()) return;
        
        synchronized (packetQueue) {
            Packet<?> packet = null;
            while ((packet = packetQueue.poll()) != null) {
                sendPacketBack(packet);
            }
        }
    }
    
    public void close() {
        if (channel.isOpen()) {
            channel.close();
            disconnect();
        }
    }
    
    public void sendPacket(Packet<?> packet) {
        if (hasClient()) {
            updateQueue();
            sendPacketBack(packet);
        } else {
            packetQueue.add(packet);
        }
    }
    
    public void setPacketListener(PacketListener packetListener) {
        listener = packetListener;
    }

    public void disconnect() {
        hasRemote = false;
        if (channel == null) return;
        
        ChannelFuture channelfuture = this.channel.disconnect();
        channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    protected boolean hasRemote() {
        return hasRemote;
    }
    
    protected void setRemote(InetSocketAddress addr) {
        if (hasRemote) {
            return;
        }
        
        hasRemote = true;
        sessionId = GameManager.nextSessionId();
        
        ChannelFuture channelfuture = channel.connect(addr);
        channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

}
