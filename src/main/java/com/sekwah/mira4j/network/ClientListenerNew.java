package com.sekwah.mira4j.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sekwah.mira4j.Mira4J;
import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.config.*;
import com.sekwah.mira4j.impl.unity.*;
import com.sekwah.mira4j.network.decoder.ClientInListener;
import com.sekwah.mira4j.network.packets.*;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage;
import com.sekwah.mira4j.network.packets.gamedata.SpawnData;
import com.sekwah.mira4j.network.packets.hazel.*;
import com.sekwah.mira4j.network.packets.net.*;
import com.sekwah.mira4j.network.packets.rpc.*;
import com.sekwah.mira4j.utils.TestUtil;

public class ClientListenerNew implements ClientInListener {
    private final ClientConnection manager;
    
    private PlayerDB client;
    private GameLobbyDB lobby;
    
    public ClientListenerNew(ClientConnection manager) {
        this.manager = manager;
    }
    
    public void onHelloPacket(HelloPacket packet) {
        if(client != null) {
            manager.sendPacket(new DisconnectPacket(DisconnectReason.CUSTOM, "Client already connected"));
            return;
        }
        
        Mira4J.LOGGER.info("A 'Hello' packet version='{}' username='{}'", TestUtil.getVersionString(packet.getVersion()), packet.getUsername());
        
        // Create a new client
        client = new PlayerDB(manager, manager.getSessionId());
        client.setName(packet.getUsername());
        
        manager.sendAcknowledgePacket(packet.getNonce());
    }
    
    public void onDisconnectPacket(DisconnectPacket packet) {
        Mira4J.LOGGER.info("A 'Disconnect' packet");
        manager.disconnect();
    }
    
    public void onReliablePacket(ReliablePacket packet) {
        if (!checkClientAccess()) return;
        manager.sendAcknowledgePacket(packet.getNonce());
        Mira4J.LOGGER.info("A 'Reliable' packet '{}' '{}'", packet.getNonce(), Arrays.toString(packet.getData()));
        
        List<HazelMessage> list = new ArrayList<>();
        PacketBuf reader = PacketBuf.wrap(packet.getData());
        try {
            HazelMessage message;
            while((message = Hazel.read(client, reader)) != null) {
                list.add(message);
            }
        } finally {
            reader.release();
        }
        
        for(HazelMessage message : list) {
            Mira4J.LOGGER.info("A '{}' packet [{}]", message.getClass().getSimpleName(), message);
            message.forwardPacket(this);
        }
    }
    
    public void onNormalPacket(NormalPacket packet) {
        if (!checkLobbyAccess()) return;
        // Mira4J.LOGGER.info("A 'Normal' packet '{}'", Arrays.toString(packet.getData()));
        
        List<HazelMessage> list = new ArrayList<>();
        PacketBuf reader = PacketBuf.wrap(packet.getData());
        try {
            HazelMessage message;
            while((message = Hazel.read(client, reader)) != null) {
                list.add(message);
            }
        } finally {
            reader.release();
        }
        
        for(HazelMessage message : list) {
            Mira4J.LOGGER.info("A '{}' packet [{}]", message.getClass().getSimpleName(), message);
            message.forwardPacket(this);
        }
    }
    
    public void onAcknowledgePacket(AcknowledgePacket packet) {
        if (!checkLobbyAccess()) return;
        
    }
    
    public void onKeepAlivePacket(PingPacket packet) {
        if (!checkLobbyAccess()) return;
        manager.sendAcknowledgePacket(packet.getNonce());
    }
    
    
    
    /**
     * @return <code>true</code> if we have a valid client and lobby session
     */
    private boolean checkLobbyAccess() {
        if (client == null || lobby == null) {
            manager.sendPacket(new DisconnectPacket(DisconnectReason.CUSTOM, "Invalid lobby session"));
            return false;
        }
        
        if (lobby != null && lobby.hasExpired()) {
            client = null;
            lobby = null;
            manager.sendPacket(new DisconnectPacket(DisconnectReason.CUSTOM, "Lobby has expired"));
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
    
    // Hazel
    public void onHostGame(HostGame packet) {
        lobby = (GameLobbyDB)GameManager.createLobby("SEKWAH");
        lobby.setSettings(packet.getGameOptionsData());
        lobby.setHost(client);
        
        manager.sendReliablePacket(HostGame.of(lobby));
    }
    
    public void onJoinGame(JoinGame packet) {
        lobby = (GameLobbyDB)GameManager.getLobby(packet.getGameId());
        if (!checkLobbyAccess()) return;
        lobby.addPlayer(client);
        
        manager.sendReliablePacket(JoinedGame.of(client, lobby));
    }
    
    public void onGameData(GameData packet) {
        for (GameDataMessage msg : packet.getMessages()) {
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
            
            // TODO: Despawn
            
            // Data should already have gotten handled but we should send plugin on move stuff.
//            if (msg instanceof Data) {
//                Data message = ((Data)msg);
//                
//                // Is -2 host ?
//                for (Component component : message.getComponents()) {
//                    lobby.getScene().addComponent(component.getScene().getPlayerFromNetId(component.getNetId()), component);
//                }
//            }
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
                    manager.sendReliablePacket(GameData.of(lobby, new RPC(netId, new SetHat(id))));
                } catch (Exception e) {}
                break;
            }
            
            case "setpet": {
                if (parts.length < 2) return;
                
                try {
                    int id = Integer.valueOf(parts[1]);
                    manager.sendReliablePacket(GameData.of(lobby, new RPC(netId, new SetPet(id))));
                } catch (Exception e) {}
                break;
            }
            
            case "setcolor": {
                if (parts.length < 2) return;
                
                try {
                    int id = Integer.valueOf(parts[1]);
                    manager.sendReliablePacket(GameData.of(lobby, new RPC(netId, new CheckColor(id))));
                } catch (Exception e) {}
                break;
            }
            
            case "setname": {
                if (parts.length < 2) return;
                
                manager.sendReliablePacket(GameData.of(lobby, new RPC(netId, new CheckName(cmd.substring(8).trim()))));
                break;
            }
            
            case "chat": {
                if (parts.length < 2) return;
                
                manager.sendReliablePacket(GameData.of(lobby, new RPC(control.getNetId(), SendChat.of(parts[1]))));
                break;
            }
            
            case "exit": {
                manager.sendPacket(new DisconnectPacket());
                break;
            }
        }
    }
    
    public void onChat(SendChat chat) {
        String cmd = chat.getMessage();
        
        if (cmd.equals("/test")) {
            for (Player p : lobby.getPlayers()) {
                PlayerControl control = ((PlayerDB)p).getComponent(PlayerControl.class);
                if (control == null) continue;
                
                GameData testing = GameData.of(lobby, new RPC(control.getNetId(), SendChat.of("[ff7f00ff]Hello World[]")));
                manager.sendReliablePacket(testing);
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
                
                PlayerControl control = client.getComponent(PlayerControl.class);
                CustomNetworkTransform cnt = client.getComponent(CustomNetworkTransform.class);
                
                if(control != null && cnt != null) {
                    cnt.lastSequenceId += 1;
                    GameData pck = GameData.of(lobby, new RPC(cnt.getNetId(), new SnapTo(next, cnt.getLastSequenceId())));
                    manager.sendReliablePacket(pck);
                }
            } catch (NumberFormatException e) {
                return;
            }
        }
        
        if (cmd.equals("/spawn")) {
            setupTest(new PlayerDB(null, GameManager.nextSessionId()));
        }
        
        if (cmd.startsWith("/name ")) {
            String name = cmd.substring(6);
            
            if (name.startsWith("c=")) {
                int index = name.indexOf(' ');
                if(index < 0) return;
                
                String color = name.substring(2, index);
                name = name.substring(index + 1);
                
                if (color.length() == 3) {
                    char a = color.charAt(0);
                    char b = color.charAt(1);
                    char c = color.charAt(2);
                    
                    color = new String(new char[] { a, a, b, b, c, c, 'f', 'f' });
                }
                
                name = "[" + color + "]" + name + "[]";
            }
            
            if (!name.isEmpty()) {
                PlayerControl control = client.getComponent(PlayerControl.class);
                if(control != null) {
                    manager.sendReliablePacket(GameData.of(lobby, new RPC(control.getNetId(), new CheckName(name))));
                }
            }
        }
    }
    
    public void setupTest(Player custom) {
        SceneDB scene = (SceneDB)lobby.getScene();
        int clientId = custom.getClientId();
        
        manager.sendReliablePacket(JoinGame.of(custom, lobby));
        lobby.addPlayer(custom);
        
        Component[] components = {
            scene.spawnComponent(PlayerControl.class, custom),
            scene.spawnComponent(PlayerPhysics.class, custom),
            scene.spawnComponent(CustomNetworkTransform.class, custom),
        };
        
        int control = components[0].getNetId();
        
        GameData data = GameData.of(lobby, new SpawnData(
            SpawnType.PLAYER_CONTROL,
            clientId,
            SpawnFlag.IS_CLIENT_CHARACTER,
            components
        ));
        
        manager.sendReliablePacket(data);
        manager.sendReliablePacket(new HazelMessage[] {
            GameDataTo.of(lobby, custom, new RPC(control, new CheckName("ServerTestBot"))),
            GameDataTo.of(lobby, custom, new RPC(control, new CheckColor(2))),
            GameData.of(lobby, new RPC(control, new SetPet(1))),
            GameData.of(lobby, new RPC(control, new SetHat(14))),
            GameData.of(lobby, new RPC(control, new SetSkin(5))),
            GameData.of(lobby, new RPC(control, new SetSkin(5))),
            // new GameData(scene, new RPC(control, new UpdateGameData(custom))),
        });
    }
    
    public void onStartGame(StartGame packet) {}
    public void onRemoveGame(RemoveGame packet) {}
    public void onRemovePlayer(RemovePlayer packet) {}
    public void onEndGame(EndGame packet) {}
    public void onAlterGame(AlterGame packet) {}
    public void onKickPlayer(KickPlayer packet) {}
    public void onWaitForHost(WaitForHost packet) {}
    public void onGameDataTo(GameDataTo packet) {}
    public void onJoinedGame(JoinedGame packet) {}
    public void onRedirect(Redirect packet) {}
    
    // RPC
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
