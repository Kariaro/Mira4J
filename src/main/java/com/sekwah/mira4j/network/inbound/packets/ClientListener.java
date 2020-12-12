package com.sekwah.mira4j.network.inbound.packets;

import com.sekwah.mira4j.Mira4J;
import com.sekwah.mira4j.config.Vector2;
import com.sekwah.mira4j.game.Lobby;
import com.sekwah.mira4j.game.Player;
import com.sekwah.mira4j.network.*;
import com.sekwah.mira4j.network.Packets.MessageType;
import com.sekwah.mira4j.network.decoder.GameDataMessage;
import com.sekwah.mira4j.network.decoder.HazelMessage;
import com.sekwah.mira4j.network.inbound.packets.hazel.GameData;
import com.sekwah.mira4j.network.inbound.packets.hazel.HostGamePacket;
import com.sekwah.mira4j.network.inbound.packets.rpc.*;
import com.sekwah.mira4j.network.inbound.packets.rpc.UpdateGameData.PlayerInfo;
import com.sekwah.mira4j.network.outbound.packets.HostGame;
import com.sekwah.mira4j.network.outbound.packets.JoinGame;
import com.sekwah.mira4j.network.outbound.packets.JoinedGame;
import com.sekwah.mira4j.utils.GameUtils;

public class ClientListener implements PacketListener {
    private final ClientConnectionManager manager;
    private final Player player;
    private final Player ghost;
    private int reliable_idx = 1;
//    private volatile int last_packet_noise = 0;
    
    public ClientListener(ClientConnectionManager manager) {
        this.manager = manager;
        this.player = new Player(0x2); // Testing
        this.ghost = new Player(0xaaaa);
    }
    
    public void onHelloPacket(HelloPacket packet) {
        Mira4J.LOGGER.info("A 'Hello' packet version='{}' username='{}'", TestUtil.getVersionString(packet.getVersion()), packet.getUsername());
        player.setUsername(packet.getUsername());
        
        AcknowledgePacket ack_packet = new AcknowledgePacket(packet.getNonce(), packet.getNonce() - 1);
        manager.sendPacket(ack_packet);
    }
    
    public void onDisconnectPacket(DisconnectPacket packet) {
        Mira4J.LOGGER.info("A 'Disconnect' packet");
        
        manager.disconnect();
    }
    
    public void onReliablePacket(ReliablePacket packet) {
//        if(last_packet_noise + 1 != packet.getNonce()) {
//            // We missed a packet send that back
//            Mira4J.LOGGER.warn("Reliable packet was lost. Implement sending missing packet!");
//        }
//        last_packet_noise = packet.getNonce();
        
        Mira4J.LOGGER.info("A 'Reliable' packet '{}' '{}'", packet.getNonce(), packet.getMessages());
        
        for(HazelMessage msg : packet.getMessages()) {
            msg.forwardPacket(this);
        }
        
        AcknowledgePacket ack_packet = new AcknowledgePacket(packet.getNonce(), -1);
        manager.sendPacket(ack_packet);
    }
    
    public void onNormalPacket(NormalPacket packet) {
        // Mira4J.LOGGER.info("A 'Normal' packet '{}'", packet.getMessages());
        
        for(HazelMessage msg : packet.getMessages()) {
            msg.forwardPacket(this);
        }
    }
    
    public void onAcknowledgePacket(AcknowledgePacket packet) {
        // Mira4J.LOGGER.info("A 'Acknowledge' packet '{}' '{}'", packet.getNonce(), packet.getMissingPackets());
    }
    
    public void onKeepAlivePacket(PingPacket packet) {
        AcknowledgePacket ack_packet = new AcknowledgePacket(packet.getNonce(), -1);
        manager.sendPacket(ack_packet);
    }
    
    
    
    public void onHostGamePacket(HostGamePacket packet) {
        Mira4J.LOGGER.info("A 'HostGamePacket' packet data='{}'", packet.getGameOptionsData());
        
        ReliablePacket send = new ReliablePacket(reliable_idx++, new HostGame(0x20));//GameCodes.codeToInt("SEKWAH")));
        manager.sendPacket(send);
    }
    
    public void onGameData(GameData packet) {
        // Mira4J.LOGGER.info("A 'GameData' packet messages={}", packet.getMessages().size());
        
        for(GameDataMessage msg : packet.getMessages()) {
            Mira4J.LOGGER.info(msg.toString());
            
            if(msg instanceof RPC) {
                RPCObject message = ((RPC)msg).getMessage();
                
                if(message instanceof SendChat) {
                    onChat((SendChat)message);
                }
            }
        }
    }
    
    public void onChat(SendChat chat) {
        String cmd = chat.getMessage();
        
        System.out.println("Command: " + cmd);
        if(cmd.equals("/test")) {
            
            RPC rcp = new RPC(ghost.getClientId(), new SendChat("[7fff00ff]Hello World[]"));
            GameData testing = new GameData(0x20, rcp);
            
            ReliablePacket send = new ReliablePacket(reliable_idx++, testing);
            manager.sendPacket(send);
        }
        
        if(cmd.equals("/spawn")) {
            setupTest();
        }
        
        if(cmd.startsWith("/check")) {
//            String name = cmd.substring(6);
//            
//            if(!name.isEmpty()) {
//                
//            }
            
            int gameId = 0x20;
            GameData[] array = {
                new GameData(gameId, new RPC(2, new MurderPlayer(4))),
                // new GameData(gameId, new Despawn(2)),
            };
            
            for(GameData data : array) {
                ReliablePacket send = new ReliablePacket(reliable_idx++, data);
                manager.sendPacket(send);
            }
        }
        
        if(cmd.equals("/exit")) {
            manager.sendPacket(new DisconnectPacket());
        }
    }

    public void onJoinGame(JoinGame packet) {
        Mira4J.LOGGER.info("A 'JoinGame' packet gameId='{}' ownedMaps='{}'", GameUtils.getStringFromGameId(packet.getGameId()), packet.getOwnedMaps());
        
        Lobby lobby = new Lobby(packet.getGameId());
        lobby.setHostId(player.getClientId());
        lobby.addPlayer(player);
        lobby.addPlayer(ghost);
        
        ReliablePacket send = new ReliablePacket(reliable_idx++, new JoinedGame(player, lobby));
        manager.sendPacket(send);
    }
    
    public void setupTest() {
        int gameId = 0x20;
        int ghostId = ghost.getClientId();
        int playrId = player.getClientId();
        
        JoinGame test2 = new JoinGame(gameId, ghostId, player.getClientId());
        ReliablePacket send1 = new ReliablePacket(reliable_idx++, test2);
        manager.sendPacket(send1);
        
        PlayerInfo info = new PlayerInfo(
            2,
            "Terminator",
            11, 1, 18, 5, 0
        );
        GameData[] array = {
            new GameData(gameId, new RPC(ghostId, new UpdateGameData(info))),
            new GameData(gameId, new RPC(ghostId, new SnapTo(new Vector2(0, 0), 39)))
        };
        
        for(GameData data : array) {
            ReliablePacket pck = new ReliablePacket(reliable_idx++, data);
            manager.sendPacket(pck);
        }
        
        HazelMessage testMessage = new HazelMessage(MessageType.GameData) {
            protected void writeData0(PacketBuf writer) {
                writer.writeInt(gameId);
                
                // Component Structure
                
                // Message
                PacketBuf buf = PacketBuf.create(4096);
                {
                    buf.writeUnsignedPackedInt(0x04);    // PlayerController PrefabId
                    buf.writeUnsignedPackedInt(ghostId); // OwnerId
                    buf.writeByte(1); // SpawnFlags
                    buf.writeUnsignedPackedInt(1); // 1 Component
                    
                    // PlayerInfo
                    buf.writeUnsignedPackedInt(ghostId); // NetId
                    PacketBuf info = PacketBuf.create(4096); {
                        info.writeByte(0); // IsNew
                        info.writeByte(1); // PlayerId
                        
                        info.writeUnsignedPackedInt(0);
                        info.writeShort(0);
                        info.writeByte(0);
                        info.writeUnsignedPackedInt(0);
                        info.writeShort(0);
                        info.writeByte(0);
                        
                        info.writeShort(1); // SequenceId
                        info.writeVector2(new Vector2(0, 0)); // TargetPos
                        info.writeVector2(new Vector2(0, 0)); // Velocity
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
        };
        
        ReliablePacket pck = new ReliablePacket(reliable_idx++, testMessage);
        manager.sendPacket(pck);
    }
}
