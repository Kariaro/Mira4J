package com.sekwah.mira4j.network;

import java.util.concurrent.atomic.AtomicInteger;

import com.sekwah.mira4j.Mira4J;
import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.config.*;
import com.sekwah.mira4j.impl.unity.GameLobby;
import com.sekwah.mira4j.impl.unity.GameManager;
import com.sekwah.mira4j.impl.unity.PlayerDB;
import com.sekwah.mira4j.network.decoder.ClientInListener;
import com.sekwah.mira4j.network.packets.*;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage;
import com.sekwah.mira4j.network.packets.gamedata.SpawnData;
import com.sekwah.mira4j.network.packets.hazel.*;
import com.sekwah.mira4j.network.packets.net.*;
import com.sekwah.mira4j.network.packets.rpc.*;
import com.sekwah.mira4j.utils.GameUtils;
import com.sekwah.mira4j.utils.TestUtil;

public class ClientListener implements ClientInListener {
    private final GameManager gameManager = GameManager.INSTANCE;
    private final ClientConnectionManager manager;
    
    private PlayerDB client;
    private GameLobby lobby;
    
    private int reliable_idx = 1;
    
    public ClientListener(ClientConnectionManager manager) {
        this.manager = manager;
    }
    
    public void onHelloPacket(HelloPacket packet) {
        Mira4J.LOGGER.info("A 'Hello' packet version='{}' username='{}'", TestUtil.getVersionString(packet.getVersion()), packet.getUsername());
        
        client = new PlayerDB(manager.getSessionId());
        client.setName(packet.getUsername());
        
        AcknowledgePacket ack_packet = new AcknowledgePacket(packet.getNonce(), packet.getNonce() - 1);
        manager.sendPacket(ack_packet);
    }
    
    public void onDisconnectPacket(DisconnectPacket packet) {
        Mira4J.LOGGER.info("A 'Disconnect' packet");
        manager.disconnect();
    }
    
    public void onReliablePacket(ReliablePacket packet) {
        if (!checkClientAccess()) return;
        Mira4J.LOGGER.info("A 'Reliable' packet '{}' '{}'", packet.getNonce(), packet.getMessages());
        
        AcknowledgePacket ack_packet = new AcknowledgePacket(packet.getNonce(), -1);
        manager.sendPacket(ack_packet);
        
        for (HazelMessage msg : packet.getMessages()) {
            msg.forwardPacket(this);
        }
    }
    
    public void onNormalPacket(NormalPacket packet) {
        if (!checkLobbyAccess()) return;
        
        // Mira4J.LOGGER.info("A 'Normal' packet '{}'", packet.getMessages());
        
        for (HazelMessage msg : packet.getMessages()) {
            msg.forwardPacket(this);
        }
    }
    
    public void onAcknowledgePacket(AcknowledgePacket packet) {
        if (!checkLobbyAccess()) return;
        // Mira4J.LOGGER.info("A 'Acknowledge' packet '{}' '{}'", packet.getNonce(), packet.getMissingPackets());
    }
    
    public void onKeepAlivePacket(PingPacket packet) {
        if (!checkLobbyAccess()) return;
        
        AcknowledgePacket ack_packet = new AcknowledgePacket(packet.getNonce(), -1);
        manager.sendPacket(ack_packet);
    }
    
    public void onHostGame(HostGame packet) {
        Mira4J.LOGGER.info("A 'HostGame' packet data='{}'", packet.getGameOptionsData());
        
        lobby = gameManager.createLobby("SEKWAH");
        lobby.setSettings(packet.getGameOptionsData());
        lobby.setHost(client);
        
        ReliablePacket send = new ReliablePacket(reliable_idx++, new HostGame(lobby));
        manager.sendPacket(send);
    }
    
    public void onJoinGame(JoinGame packet) {
        Mira4J.LOGGER.info("A 'JoinGame' packet gameId='{}' ownedMaps='{}'", GameUtils.getStringFromGameId(packet.getGameId()), packet.getOwnedMaps());
        
        lobby = gameManager.getLobby(packet.getGameId());
        lobby.addPlayer(client);
        
        ReliablePacket send = new ReliablePacket(reliable_idx++, new JoinedGame(client, lobby));
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
                    testCmd(packet.getGameId(), (RPC)msg, (SendChat)message);
                }
            }
            
            if (msg instanceof SpawnData) {
                SpawnData message = ((SpawnData)msg);
                
                // Is -2 host ?
                Player player = lobby.getPlayerByClientId(message.getOwnerClientId());
                if (player != null) {
                    for (Component component : message.getComponents()) {
                        lobby.getScene().addComponent(player, component);
                    }
                }
            }
        }
    }
    
    private void testCmd(int gameId, RPC msg, SendChat chat) {
        PlayerControl control = client.getComponent(PlayerControl.class);
        if (control == null) return;
        
        String cmd = chat.getMessage();
        if (!cmd.startsWith("/")) return;
        cmd = cmd.substring(1);
        
        String[] parts = cmd.split(" ");
        if (parts.length < 1) return;
        
        // Check that the component has a reference to this net id!
        int netId = msg.getSenderNetId();
        // A component always has a reference to a player and should only be created inside a player
        // the netId is given by the host of the game and not the server?
        
        switch (parts[0]) {
            case "sethat": {
                if (parts.length < 2) return;
                
                try {
                    int id = Integer.valueOf(parts[1]);
                    ReliablePacket rpck = new ReliablePacket(reliable_idx++, new GameData(gameId, new RPC(netId, new SetHat(id))));
                    manager.sendPacket(rpck);
                } catch (Exception e) {}
                break;
            }
            
            case "setpet": {
                if (parts.length < 2) return;
                
                try {
                    int id = Integer.valueOf(parts[1]);
                    ReliablePacket rpck = new ReliablePacket(reliable_idx++, new GameData(gameId, new RPC(netId, new SetPet(id))));
                    manager.sendPacket(rpck);
                } catch (Exception e) {}
                break;
            }
            
            case "setcolor": {
                if (parts.length < 2) return;
                
                try {
                    int id = Integer.valueOf(parts[1]);
                    ReliablePacket rpck = new ReliablePacket(reliable_idx++, new GameData(gameId, new RPC(netId, new CheckColor(id))));
                    manager.sendPacket(rpck);
                } catch (Exception e) {}
                break;
            }
            
            case "setname": {
                if (parts.length < 2) return;
                
                ReliablePacket rpck = new ReliablePacket(reliable_idx++, new GameData(gameId, new RPC(netId, new CheckName(cmd.substring(8).trim()))));
                manager.sendPacket(rpck);
                break;
            }
            
            case "chat": {
                if (parts.length < 2) return;
                
                PlayerDB dummy = new PlayerDB(GameManager.nextSessionId());
                setupTest(dummy);
                
                ReliablePacket rpck = new ReliablePacket(reliable_idx++, new GameData(gameId, new RPC(dummy.getComponent(PlayerControl.class).getNetId(), SendChat.of(parts[1]))));
                manager.sendPacket(rpck);
                break;
            }
            
            case "exit": {
                manager.sendPacket(new DisconnectPacket());
                break;
            }
        }
    }
    
    public void onChat(SendChat chat) {
        int gameId = lobby.getGameId();
        String cmd = chat.getMessage();
        
        if (cmd.equals("/test")) {
            for (Player p : lobby.getPlayers()) {
                PlayerControl control = ((PlayerDB)p).getComponent(PlayerControl.class);
                if (control == null) continue;
                
                GameData testing = new GameData(gameId, new RPC(control.getNetId(), SendChat.of("[ff7f00ff]Hello World[]")));
                ReliablePacket send = new ReliablePacket(reliable_idx++, testing);
                manager.sendPacket(send);
            }
        }
        
        if (cmd.startsWith("/tp ")) {
            String str_cmd = cmd.substring(4);
            
            String[] part = str_cmd.split(" ");
            
            if(part.length != 2) return;
            float x = 0;
            float y = 0;
            
            try {
                x = (float)(double)Double.valueOf(part[0]);
                y = (float)(double)Double.valueOf(part[1]);
                Vector2 next = new Vector2(x, y);
                
                for (Player p : lobby.getPlayers()) {
                    PlayerControl control = ((PlayerDB)p).getComponent(PlayerControl.class);
                    CustomNetworkTransform cnt = ((PlayerDB)p).getComponent(CustomNetworkTransform.class);
                    if(control == null || cnt == null) continue;
                    
                    cnt.lastSequenceId += 1;
                    GameData pck = new GameData(gameId, new RPC(cnt.getNetId(), new SnapTo(next, cnt.getLastSequenceId())));
                    ReliablePacket rpck = new ReliablePacket(reliable_idx++, pck);
                    manager.sendPacket(rpck);
                }
            } catch (NumberFormatException e) {
                return;
            }
        }
        
        if (cmd.equals("/spawn")) {
            setupTest(new PlayerDB(GameManager.nextSessionId()));
        }
        
        if (cmd.startsWith("/name ")) {
            String name = cmd.substring(6);
            
            if (name.startsWith("c=")) {
                int index = name.indexOf(' ');
                if(index < 0) return;
                
                String color = name.substring(2, index);
                name = name.substring(index);
                
                if (color.length() == 3) {
                    char a = color.charAt(0);
                    char b = color.charAt(1);
                    char c = color.charAt(2);
                    
                    color = new String(new char[] { a, a, b, b, c, c, 'f', 'f' });
                }
                
                name = "[" + color + "]" + name + "[]";
            }
            
            if (!name.isEmpty()) {
                for (Player p : lobby.getPlayers()) {
                    PlayerControl control = p.getComponent(PlayerControl.class);
                    if(control == null) continue;
                    
                    GameData testing = new GameData(gameId, new RPC(control.getNetId(), new CheckName(name)));
                    ReliablePacket send = new ReliablePacket(reliable_idx++, testing);
                    manager.sendPacket(send);
                }
            }
        }
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
    
    private AtomicInteger atomId = new AtomicInteger(10);
    
    public void setupTest(Player custom) {
        int gameId = lobby.getGameId();
        Scene scene = lobby.getScene();
        int clientId = custom.getClientId();
        
        ReliablePacket join_packet = new ReliablePacket(reliable_idx++, new JoinGame(gameId, clientId, client.getClientId()));
        manager.sendPacket(join_packet);
        lobby.addPlayer(custom);
        
        int control = atomId.getAndIncrement();
        Component[] components = new Component[3];
        
        {
            components[0] = new PlayerControl(control, lobby.getNumPlayers() + 1, false);
            components[1] = new PlayerPhysics(atomId.getAndIncrement());
            components[2] = new CustomNetworkTransform(atomId.getAndIncrement());
            
            for (Component c : components) {
                lobby.getScene().addComponent(custom, c);
            }
        }
        
        GameData data = new GameData(scene, new SpawnData(
            SpawnType.PLAYER_CONTROL,
            clientId,
            SpawnFlag.IS_CLIENT_CHARACTER,
            components
        ));
        
        ReliablePacket big_pck = new ReliablePacket(reliable_idx++, data);
        manager.sendPacket(big_pck);
        
        ReliablePacket big_pck2 = new ReliablePacket(reliable_idx++, new HazelMessage[] {
            new GameDataTo(scene, custom.getClientId(), new RPC(control, new CheckName("ServerTestBot"))),
            new GameDataTo(scene, custom.getClientId(), new RPC(control, new CheckColor(2))),
            new GameData(scene, new RPC(control, new SetPet(1))),
            new GameData(scene, new RPC(control, new SetHat(14))),
            new GameData(scene, new RPC(control, new SetSkin(5))),
        });
        manager.sendPacket(big_pck2);
    }
    
    private boolean checkLobbyAccess() {
        if (client == null || lobby == null) {
            manager.sendPacket(new DisconnectPacket(DisconnectReason.CUSTOM, "Invalid lobby session"));
            return false;
        }
        
        return true;
    }
    
    private boolean checkClientAccess() {
        if (client == null) {
            manager.sendPacket(new DisconnectPacket(DisconnectReason.CUSTOM, "Invalid client session"));
            return false;
        }
        
        return true;
    }
    
    public void onGameDataTo(GameDataTo packet) {}
    public void onJoinedGame(JoinedGame packet) {}
    public void onRedirect(Redirect packet) {}
    public void onAddVote(AddVote packet) {}
    public void onCastVote(CastVote packet) {}
    public void onCheckColor(CheckColor packet) {}
    public void onCheckName(CheckName packet) {}
    public void onClearVote(ClearVote packet) {}
    public void onClose(Close packet) {}
    public void onCloseDoorsOfType(CloseDoorsOfType packet) {}
    public void onCompleteTask(CompleteTask packet) {}
    public void onEnterVent(EnterVent packet) {}
    public void onExiled(Exiled packet) {}
    public void onExitVent(ExitVent packet) {}
    public void onMurderPlayer(MurderPlayer packet) {}
    public void onPlayAnimation(PlayAnimation packet) {}
    public void onRepairSystem(RepairSystem packet) {}
    public void onReportDeadBody(ReportDeadBody packet) {}
    public void onSendChat(SendChat packet) {}
    public void onSendChatNote(SendChatNote packet) {}
    public void onSetColor(SetColor packet) {}
    public void onSetHat(SetHat packet) {}
    public void onSetInfected(SetInfected packet) {}
    public void onSetName(SetName packet) {}
    public void onSetPet(SetPet packet) {}
    public void onSetScanner(SetScanner packet) {}
    public void onSetSkin(SetSkin packet) {}
    public void onSetStartCounter(SetStartCounter packet) {}
    public void onSetTasks(SetTasks packet) {}
    public void onSnapTo(SnapTo packet) {}
    public void onStartMeeting(StartMeeting packet) {}
    public void onSyncSettings(SyncSettings packet) {}
    public void onUpdateGameData(UpdateGameData packet) {}
    public void onVotingComplete(VotingComplete packet) {}
}
