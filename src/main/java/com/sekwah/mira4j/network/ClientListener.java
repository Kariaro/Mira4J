package com.sekwah.mira4j.network;

import com.sekwah.mira4j.Mira4J;
import com.sekwah.mira4j.config.Vector2;
import com.sekwah.mira4j.game.GameLobby;
import com.sekwah.mira4j.game.GameManager;
import com.sekwah.mira4j.game.Player;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.decoder.GameDataMessage;
import com.sekwah.mira4j.network.inbound.packets.*;
import com.sekwah.mira4j.network.inbound.packets.hazel.*;
import com.sekwah.mira4j.network.inbound.packets.rpc.*;
import com.sekwah.mira4j.network.inbound.packets.rpc.UpdateGameData.PlayerInfo;
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
        
        for (HazelMessage msg : packet.getMessages()) {
            msg.forwardPacket(this);
        }
        
        AcknowledgePacket ack_packet = new AcknowledgePacket(packet.getNonce(), -1);
        manager.sendPacket(ack_packet);
    }
    
    public void onNormalPacket(NormalPacket packet) {
        // Mira4J.LOGGER.info("A 'Normal' packet '{}'", packet.getMessages());
        
        for (HazelMessage msg : packet.getMessages()) {
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
    
    
    
    public void onHostGame(HostGame packet) {
        Mira4J.LOGGER.info("A 'HostGame' packet data='{}'", packet.getGameOptionsData());
        
        // lobby = gameManager.createNewLobby();
        lobby = gameManager.createLobby("SEKWAH");
        lobby.setSettings(packet.getGameOptionsData());
        lobby.setHost(player);
        
        // //GameCodes.codeToInt("SEKWAH")));
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
        }
    }
    
    public void onChat(SendChat chat) {
        if (lobby == null) return;
        int gameId = lobby.getGameId();
        String cmd = chat.getMessage();
        
        if (cmd.equals("/test")) {
            RPC rcp = new RPC(ghost.getClientId(), new SendChat("[7fff00ff]Hello World[]"));
            GameData testing = new GameData(gameId, rcp);
            
            ReliablePacket send = new ReliablePacket(reliable_idx++, testing);
            manager.sendPacket(send);
        }
        
        if (cmd.equals("/spawn")) {
            setupTest();
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
    
    
    public void setupTest() {
        int gameId = lobby.getGameId();
        int ghostId = ghost.getClientId();
        
        ReliablePacket send1 = new ReliablePacket(reliable_idx++, new JoinGame(gameId, ghostId, player.getClientId()));
        manager.sendPacket(send1);
        
        PlayerInfo info = new PlayerInfo(2, "Terminator", 11, 1, 18, 5, 0);
        
        GameData[] array = {
            new GameData(lobby, new RPC(ghostId, new SetName("Hugo"))),
            new GameData(lobby, new RPC(ghostId, new UpdateGameData(info))),
//            new GameData(lobby, new RPC(ghostId, new SnapTo(new Vector2(0, 0), 39)))
        };
        
        for (GameData data : array) {
            ReliablePacket pck = new ReliablePacket(reliable_idx++, data);
            manager.sendPacket(pck);
        }
        
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
            
            public int id() {
                return HazelType.GameData.getId();
            }
        };
        
        ReliablePacket pck = new ReliablePacket(reliable_idx++, testMessage);
        manager.sendPacket(pck);
    }
}
