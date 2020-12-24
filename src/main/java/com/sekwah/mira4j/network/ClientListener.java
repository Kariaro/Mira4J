package com.sekwah.mira4j.network;

import java.util.Arrays;

import com.sekwah.mira4j.Mira4J;
import com.sekwah.mira4j.config.*;
import com.sekwah.mira4j.game.GameLobby;
import com.sekwah.mira4j.game.GameManager;
import com.sekwah.mira4j.game.Player;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.packets.*;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage;
import com.sekwah.mira4j.network.packets.gamedata.SpawnData;
import com.sekwah.mira4j.network.packets.hazel.*;
import com.sekwah.mira4j.network.packets.net.CustomNetworkTransform;
import com.sekwah.mira4j.network.packets.net.PlayerControl;
import com.sekwah.mira4j.network.packets.net.PlayerPhysics;
import com.sekwah.mira4j.network.packets.rpc.*;
import com.sekwah.mira4j.utils.GameUtils;
import com.sekwah.mira4j.utils.TestUtil;

public class ClientListener implements PacketListener {
    private final ClientConnectionManager manager;
    private final GameManager gameManager;
    private GameLobby lobby;
    
    // TODO: Check if player was connected or not!
    private final Player player;
    private final Player ghost;
    private int reliable_idx = 1;
    
    public ClientListener(GameManager gameManager, ClientConnectionManager manager) {
        this.manager = manager;
        this.gameManager = gameManager;
        
        this.player = gameManager.newPlayer();
        this.ghost = new Player(0xaaaa);
    }
    
    public void onHelloPacket(HelloPacket packet) {
        Mira4J.LOGGER.info("A 'Hello' packet version='{}' username='{}'", TestUtil.getVersionString(packet.getVersion()), packet.getUsername());
        player.setName(packet.getUsername());
        
        AcknowledgePacket ack_packet = new AcknowledgePacket(packet.getNonce(), packet.getNonce() - 1);
        manager.sendPacket(ack_packet);
    }
    
    public void onDisconnectPacket(DisconnectPacket packet) {
        Mira4J.LOGGER.info("A 'Disconnect' packet");
        manager.disconnect();
    }
    
    public void onReliablePacket(ReliablePacket packet) {
        Mira4J.LOGGER.info("A 'Reliable' packet '{}' '{}'", packet.getNonce(), packet.getMessages());
        
        AcknowledgePacket ack_packet = new AcknowledgePacket(packet.getNonce(), -1);
        manager.sendPacket(ack_packet);
        
        for (HazelMessage msg : packet.getMessages()) {
            msg.forwardPacket(this);
        }
    }
    
    public void onNormalPacket(NormalPacket packet) {
        // Mira4J.LOGGER.info("A 'Normal' packet '{}'", packet.getMessages());
        
        for (HazelMessage msg : packet.getMessages()) {
            msg.forwardPacket(this);
        }
        
        System.out.println("Lobby: " + lobby.scene);
    }
    
    public void onAcknowledgePacket(AcknowledgePacket packet) {
        // Mira4J.LOGGER.info("A 'Acknowledge' packet '{}' '{}'", packet.getNonce(), packet.getMissingPackets());
    }
    
    public void onKeepAlivePacket(PingPacket packet) {
        AcknowledgePacket ack_packet = new AcknowledgePacket(packet.getNonce(), -1);
        manager.sendPacket(ack_packet);
    }
    
    
    
    public void onHostGame(HostGame packet) {
        Mira4J.LOGGER.info("A 'HostGame' packet data='{}'", packet.getGameOptionsData());
        
        lobby = gameManager.createLobby("SEKWAH");
        lobby.setSettings(packet.getGameOptionsData());
        lobby.setHost(player);
        
        ReliablePacket send = new ReliablePacket(reliable_idx++, new HostGame(lobby));
        manager.sendPacket(send);
    }
    
    public void onGameData(GameData packet) {
        // Mira4J.LOGGER.info("A 'GameData' packet messages={}", packet.getMessages().size());
        
        for (GameDataMessage msg : packet.getMessages()) {
            Mira4J.LOGGER.info(msg.toString());
            
            if (msg instanceof RPC) {
                RPCMessage message = ((RPC)msg).getMessage();
                
                if (message instanceof SendChat) {
                    onChat((SendChat)message);
                }
            }
            
            if (msg instanceof SpawnData) {
                SpawnData message = ((SpawnData)msg);
                
                // Is -2 host ?
                Mira4J.LOGGER.info("Spawning");
                Mira4J.LOGGER.info("    : {}", message.getOwnerClientId());
            }
        }
    }
    
    public void onChat(SendChat chat) {
        if (lobby == null) return;
        int gameId = lobby.getGameId();
        String cmd = chat.getMessage();
        
        if (cmd.equals("/test")) {
            for (int i = 0; i < 10; i++) {
                RPC rcp = new RPC(i /*ghost.getClientId()*/, new SendChat("Hello World"));
                GameData testing = new GameData(gameId, rcp);
                
                ReliablePacket send = new ReliablePacket(reliable_idx++, testing);
                manager.sendPacket(send);
            }
        }
        
        if (cmd.equals("/spawn")) {
            Player player = gameManager.newPlayer();
            for (int i = 0; i < 1; i++) {
                setupTest(player);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        
        if (cmd.startsWith("/name ")) {
            String name = cmd.substring(6);
            
            if (!name.isEmpty()) {
                GameData[] array = {
                    new GameData(gameId, new RPC(ghost.getClientId(), new SetName("Termin"))),
                    new GameData(gameId, new RPC(ghost.getClientId(), new UpdateGameData(new PlayerInfo(
                        2, name, 11, 0, 1, 0, 0
                    ))))
                };
                
                for (GameData data : array) {
                    ReliablePacket send = new ReliablePacket(reliable_idx++, data);
                    manager.sendPacket(send);
                }
            }
        }
        
        if (cmd.equals("/exit")) {
            manager.sendPacket(new DisconnectPacket());
        }
    }

    public void onJoinGame(JoinGame packet) {
        Mira4J.LOGGER.info("A 'JoinGame' packet gameId='{}' ownedMaps='{}'", GameUtils.getStringFromGameId(packet.getGameId()), packet.getOwnedMaps());
        
        lobby = gameManager.getLobby(packet.getGameId());
        lobby.addPlayer(player);
        
        ReliablePacket send = new ReliablePacket(reliable_idx++, new JoinedGame(player, lobby));
        manager.sendPacket(send);
    }
    
    public void onStartGame(StartGame packet) {
        Mira4J.LOGGER.info("A 'StartGame' packet gameId='{}'", GameUtils.getStringFromGameId(packet.getGameId()));
    }
    
    public void onRemoveGame(RemoveGame packet) {
        Mira4J.LOGGER.info("A 'RemoveGame' packet");
    }
    
    public void onRemovePlayer(RemovePlayer packet) {
        Mira4J.LOGGER.info("A 'RemovePlayer' packet gameId='{}' disconnectedClientId='{}' disconnectReason='{}'", packet.getGameId(), packet.getDisconnectedClientId(), packet.getDisconnectReason());
    }
    
    public void onEndGame(EndGame packet) {
        Mira4J.LOGGER.info("A 'EndGame' packet gameId='{}' gameOverReason='{}' showAd='{}'", packet.getGameId(), packet.getGameOverReason(), packet.getShowAd());
    }
    
    public void onAlterGame(AlterGame packet) {
        Mira4J.LOGGER.info("A 'AlterGame' packet gameId='{}' tagId='{}' tagValue='{}'", packet.getGameId(), packet.getTagId(), packet.getTagValue());
    }
    
    public void onKickPlayer(KickPlayer packet) {
        Mira4J.LOGGER.info("A 'KickPlayer' packet gameId='{}' kickedClientId='{}' isBanned='{}' disconnectReason='{}'", packet.getGameId(), packet.getKickedClientId(), packet.isBanned(), packet.getDisconnectReason());
    }
    
    public void onWaitForHost(WaitForHost packet) {
        Mira4J.LOGGER.info("A 'WaitForHost' packet gameId='{}' rejoiningClientId='{}'", packet.getGameId(), packet.getRejoiningClientId());
    }
    
    
    public void setupTest(Player player) {
        int gameId = lobby.getGameId();
        int ghostId = player.getClientId();
        
        ReliablePacket send1 = new ReliablePacket(reliable_idx++, new JoinGame(gameId, ghostId, this.player.getClientId()));
        manager.sendPacket(send1);
        
        GameData[] array = {
            new GameData(lobby, new RPC(7, new SetName("Hugo"))),
            new GameData(lobby, new RPC(7, new SetColor(2))),
            new GameData(lobby, new RPC(7, new SetPet(1))),
            new GameData(lobby, new RPC(7, new SetHat(18))),
            new GameData(lobby, new RPC(7, new SetSkin(5)))
            // , new GameData(lobby, new RPC(2, new UpdateGameData(new PlayerInfo(2, "Hugo", 11, 1, 18, 5, 0)))),
        };
        
        for (GameData data : array) {
            ReliablePacket pck2 = new ReliablePacket(reliable_idx++, data);
            manager.sendPacket(pck2);
        }
        
        GameData data = new GameData(lobby, new SpawnData(
            SpawnType.PLAYER_CONTROL,
            ghostId,
            SpawnFlag.IS_CLIENT_CHARACTER,
            new PlayerControl(100, player.getClientId(), true),
            new PlayerPhysics(101),
            new CustomNetworkTransform(102)
        ));
        
        {
        HazelMessage testMessage = new HazelMessage() {
            public void forwardPacket(ClientListener listener) {}
            public void read(PacketBuf reader) {}
            
            public void write(PacketBuf writer) {
                writer.writeInt(gameId);
                
                // Component Structure
                
                // Message
                PacketBuf buf = PacketBuf.create(4096);
                {
                    buf.writeUnsignedPackedInt(0x04);    // PlayerController PrefabId
                    buf.writeUnsignedPackedInt(ghostId); // OwnerId
                    buf.writeByte(SpawnFlag.IS_CLIENT_CHARACTER); // SpawnFlags
                    buf.writeUnsignedPackedInt(3); // 3 Components
                    
                    // PlayerInfo
                    buf.writeUnsignedPackedInt(ghostId); // NetId
                    PacketBuf info = PacketBuf.create(4096); {
                        info.writeByte(0); // IsNew
                        info.writeByte(ghostId); // PlayerId
                        
                        info.writeUnsignedPackedInt(0);
                        info.writeShort(0);
                        info.writeByte(0);
                        info.writeUnsignedPackedInt(0);
                        info.writeShort(0);
                        info.writeByte(0);
                        
                        info.writeShort(1); // SequenceId
                        info.writeVector2(new Vector2(0, 0)); // TargetPos
                        info.writeVector2(new Vector2(0.5f, 20)); // Velocity
                    }
                    byte[] bytes = info.readBytes(info.readableBytes());
                    info.release();
                    
                    buf.writeShort(bytes.length);
                    buf.writeByte(0x04); // PlayerController
                    buf.writeBytes(bytes);
                }
                byte[] bytes = buf.readBytes(buf.readableBytes());
                buf.release();
                
                writer.writeShort(bytes.length);
                writer.writeByte(0x04); // Spawn
                writer.writeBytes(bytes);
            }
            
            public int id() {
                return HazelType.GameData.getId();
            }
        };
            
            PacketBuf bufA = PacketBuf.create(65536);
            testMessage.write(bufA);
            
            PacketBuf bufB = PacketBuf.create(65536);            data.write(bufB);
            
            byte[] arrayA = bufA.readBytes(bufA.readableBytes());
            byte[] arrayB = bufB.readBytes(bufB.readableBytes());
            bufA.release();
            bufB.release();
            
            System.out.println("A: " + Arrays.toString(arrayA));
            System.out.println("B: " + Arrays.toString(arrayB));
        }
        ReliablePacket pck = new ReliablePacket(reliable_idx++, data);
        manager.sendPacket(pck);
    }
}
