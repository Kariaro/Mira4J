package com.sekwah.mira4j.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sekwah.mira4j.Mira4J;
import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.config.*;
import com.sekwah.mira4j.impl.unity.*;
import com.sekwah.mira4j.network.decoder.ClientInListener;
import com.sekwah.mira4j.network.packets.*;
import com.sekwah.mira4j.network.packets.gamedata.DataMessage;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage.SceneChange;
import com.sekwah.mira4j.network.packets.gamedata.SpawnMessage;
import com.sekwah.mira4j.network.packets.hazel.*;
import com.sekwah.mira4j.network.packets.net.*;
import com.sekwah.mira4j.network.packets.rpc.*;
import com.sekwah.mira4j.plugin.api.ChatEvent;
import com.sekwah.mira4j.plugin.api.MoveEvent;
import com.sekwah.mira4j.plugin.api.PluginTester;
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
        client.name = packet.getUsername();
        
        manager.sendAcknowledgePacket(packet.getNonce());
    }
    
    public void onDisconnectPacket(DisconnectPacket packet) {
        Mira4J.LOGGER.info("A 'Disconnect' packet");
        manager.disconnect();
        
        if(lobby != null) {
            lobby.removePlayer(client);
        }
        
        lobby = null;
        client = null;
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
            // Mira4J.LOGGER.info("A '{}' packet [{}]", message.getClass().getSimpleName(), message);
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
//        PlayerDB db = new PlayerDB(null, 0);
//        lobby.setHost(db);
//        lobby.addPlayer(db);
        
        manager.sendReliablePacket(HostGame.of(lobby));
    }
    
    public void onJoinGame(JoinGame packet) {
        lobby = (GameLobbyDB)GameManager.getLobby(packet.getGameId());
        if (!checkLobbyAccess()) return;
        lobby.addPlayer(client);
        
        manager.sendReliablePacket(JoinedGame.of(client, lobby));
        // spawnPlayer(client);
    }
    
    public void onGameData(GameData packet) {
        for (GameDataMessage msg : packet.getMessages()) {
            // msg.forwardPacket(this);
            
            if (msg instanceof RPC) {
                msg.forwardPacket(this);
                
                RPCMessage message = ((RPC)msg).getMessage();
                
                if (message instanceof CheckName) {
                    client.setName(((CheckName)msg).getName());
                }
                
                if (message instanceof CheckColor) {
                    client.setColorId(((CheckColor)msg).getColorId());
                    manager.sendReliablePacket(GameData.of(lobby, new RPC(client.getClientId(), new UpdateGameData(client.getScene()))));
                }
            }
            
            if (msg instanceof SpawnMessage) {
                SpawnMessage message = (SpawnMessage)msg;
                
                // Is -2 host ?
                Player player = lobby.getPlayerByClientId(message.getOwnerClientId());
                for (Component component : message.getComponents()) {
                    lobby.getScene().addComponent(player, component);
                }
            }
            
            if (msg instanceof DataMessage) {
                DataMessage message = (DataMessage)msg;
                
                for (Component component : message.getComponents()) {
                    
                    if(component instanceof CustomNetworkTransform) {
                        CustomNetworkTransform cnt = (CustomNetworkTransform)component;
                        
                        PluginTester.doEvent(new MoveEvent(component.getPlayer(), cnt.targetPosition, cnt.targetVelocity));
                    }
                }
            }
            
            if (msg instanceof SceneChange) {
//                manager.sendReliablePacket(new HazelMessage[] {
//                    GameDataTo.of(lobby, client, new RPC(control, new CheckName(c))),
//                    GameDataTo.of(lobby, client, new RPC(control, new CheckColor(2))),
//                    GameData.of(lobby, new RPC(control, new SetPet(1))),
//                    GameData.of(lobby, new RPC(control, new SetHat(14))),
//                    GameData.of(lobby, new RPC(control, new SetSkin(5))),
//                    // new GameData(scene, new RPC(control, new UpdateGameData(custom))),
//                });
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
    
    private void spawnPlayer(Player player) {
        SceneDB scene = (SceneDB)lobby.getScene();
        int clientId = client.getClientId();
        
        client.setName(client.getName());
        client.setColorId(2);
        client.setPetId(1);
        client.setSkinId(5);
        
        {
            LobbyBehaviour lb = new LobbyBehaviour();
            lb.load(scene, null);
            lb.setNetId(1);
            
            NetGameData nd = new NetGameData();
            nd.load(scene, null);
            nd.setNetId(2);
            
            VoteBanSystem vb = new VoteBanSystem();
            vb.load(scene, null);
            vb.setNetId(3);
            
            Component[] components = {
                scene.spawnComponent(PlayerControl.class, client),
                scene.spawnComponent(PlayerPhysics.class, client),
                scene.spawnComponent(CustomNetworkTransform.class, client),
            };
            
            manager.sendReliablePacket(
                GameData.of(lobby, new SpawnMessage(
                    SpawnType.LOBBY_BEHAVIOUR,
                    -2,
                    SpawnFlag.NONE,
                    lb
                )),
                GameData.of(lobby, new SpawnMessage(
                    SpawnType.GAME_DATA,
                    -2,
                    SpawnFlag.NONE,
                    new Component[] { nd, vb }
                )),
                GameData.of(lobby, new SpawnMessage(
                    SpawnType.PLAYER_CONTROL,
                    clientId,
                    SpawnFlag.IS_CLIENT_CHARACTER,
                    components
                )),
                GameData.of(lobby, new RPC(player.getClientId(), new UpdateGameData(player.getScene())))
            );
            
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    
                    CustomNetworkTransform cnt = client.getComponent(CustomNetworkTransform.class);
                    cnt.lastSequenceId = 0;
                    for(int i = 0; i < 3; i++) {
                        Thread.sleep(200);
                        cnt.lastSequenceId ++;
                        manager.sendReliablePacket(
                            GameData.of((i == 0) ? (null):(lobby), new DataMessage(client.getScene(), cnt))
                        );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.setDaemon(true);
            thread.start();
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
                
                client.setName(cmd.substring(8).trim());
                // manager.sendReliablePacket(GameData.of(lobby, new RPC(netId, new CheckName(cmd.substring(8).trim()))));
                
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
            
            case "tc": {
                PlayerDB db = new PlayerDB(null, GameManager.nextSessionId());
                manager.sendReliablePacket(JoinGame.of(db, lobby));
                manager.sendReliablePacket(StartGame.of(lobby));
            }
        }
    }
    
    public void setupSpawnTest(Player player) {
//        SceneDB scene = (SceneDB)lobby.getScene();
        int clientId = player.getClientId();
        
        manager.sendReliablePacket(JoinGame.of(player, lobby));
        lobby.addPlayer(player);
        manager.sendReliablePacket(GameData.of(lobby, new SceneChange(player.getClientId(), "OnlineGame")));
//        manager.sendReliablePacket(new HazelMessage[] {
//            GameDataTo.of(lobby, player, new RPC(clientId, new CheckName("ServerTestBot"))),
//            GameDataTo.of(lobby, player, new RPC(clientId, new CheckColor(2))),
//            GameData.of(lobby, new RPC(clientId, new SetPet(1))),
//            GameData.of(lobby, new RPC(clientId, new SetHat(14))),
//            GameData.of(lobby, new RPC(clientId, new SetSkin(5)))
//        });
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
                
                PlayerControl cn = player.getComponent(PlayerControl.class);
                int control = cn.getNetId();
                CustomNetworkTransform cnt = player.getComponent(CustomNetworkTransform.class);
                
                ((PlayerDB)player).setDirty(true);
                manager.sendReliablePacket(new HazelMessage[] {
                    GameDataTo.of(lobby, player, new RPC(control, new CheckName("Testing"))),
                    GameDataTo.of(lobby, player, new RPC(control, new CheckColor(2))),
                    GameData.of(lobby, new RPC(control, new SetPet(1))),
                    GameData.of(lobby, new RPC(control, new SetHat(14))),
                    GameData.of(lobby, new RPC(control, new SetSkin(5)))
                });
                
                Thread.sleep(1000);
                
                for(int i = 0; i < 100; i++) {
                    Thread.sleep(200);
                    cnt.targetVelocity.set(0.2f, 0.2f);
                    cnt.targetPosition.add(new Vector2(0.5f, 0.5f));
                    NormalPacket np = new NormalPacket(GameData.of(lobby, new DataMessage(player.getScene(), cnt)));
                    manager.sendPacket(np);
                    manager.sendReliablePacket(GameData.of(lobby, new RPC(control, new SnapTo(cnt.targetPosition.clone(), cnt.lastSequenceId))));
                    manager.sendReliablePacket(GameData.of(lobby, new RPC(control, new SyncSettings(lobby.getSettings()))));
                    cnt.lastSequenceId += 1;
                    System.out.println("Move");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
//        Component[] components = {
//            scene.spawnComponent(PlayerControl.class, player),
//            scene.spawnComponent(PlayerPhysics.class, player),
//            scene.spawnComponent(CustomNetworkTransform.class, player),
//        };
//        
//        int control = components[0].getNetId();
//        
//        GameData data = GameData.of(lobby, new SpawnMessage(
//            SpawnType.PLAYER_CONTROL,
//            clientId,
//            SpawnFlag.NONE,
//            components
//        ));
//        
//        manager.sendReliablePacket(data);
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
    public void onSendChat(RPC rpc, SendChat chat) {
        {
            testCmd(lobby.getGameId(), rpc, chat);
            
            Component cmp = lobby.getScene().getComponent(rpc.getSenderNetId());
            if(cmp != null) {
                Player owner = cmp.getPlayer();
                
                PluginTester.doEvent(new ChatEvent(owner, chat.getMessage()));
            }
        }
        
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
                    
                    Vector2 pos = cnt.targetPosition;
                    pos.x = x;
                    pos.y = y; 
                    cnt.targetVelocity.set(-0.00061f, -0.00061f);
                    manager.sendReliablePacket(GameData.of(lobby, new DataMessage(lobby.getScene(), cnt)));
                }
            } catch (NumberFormatException e) {
                return;
            }
        }
        
        if (cmd.equals("/spawn")) {
            PlayerDB db = new PlayerDB(null, GameManager.nextSessionId());
            setupSpawnTest(db);
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
    
    public void onAddVote(RPC rpc, AddVote packet) {}
    public void onCastVote(RPC rpc, CastVote packet) {}
    public void onCheckColor(RPC rpc, CheckColor packet) {}
    public void onCheckName(RPC rpc, CheckName packet) {}
    public void onClearVote(RPC rpc, ClearVote packet) {}
    public void onClose(RPC rpc, Close packet) {}
    public void onCloseDoorsOfType(RPC rpc, CloseDoorsOfType packet) {}
    public void onCompleteTask(RPC rpc, CompleteTask packet) {}
    public void onEnterVent(RPC rpc, EnterVent packet) {}
    public void onExiled(RPC rpc, Exiled packet) {}
    public void onExitVent(RPC rpc, ExitVent packet) {}
    public void onMurderPlayer(RPC rpc, MurderPlayer packet) {}
    public void onPlayAnimation(RPC rpc, PlayAnimation packet) {}
    public void onRepairSystem(RPC rpc, RepairSystem packet) {}
    public void onReportDeadBody(RPC rpc, ReportDeadBody packet) {}
    public void onSendChatNote(RPC rpc, SendChatNote packet) {}
    public void onSetColor(RPC rpc, SetColor packet) {}
    public void onSetHat(RPC rpc, SetHat packet) {}
    public void onSetInfected(RPC rpc, SetInfected packet) {}
    public void onSetName(RPC rpc, SetName packet) {}
    public void onSetPet(RPC rpc, SetPet packet) {}
    public void onSetScanner(RPC rpc, SetScanner packet) {}
    public void onSetSkin(RPC rpc, SetSkin packet) {}
    public void onSetStartCounter(RPC rpc, SetStartCounter packet) {}
    public void onSetTasks(RPC rpc, SetTasks packet) {}
    public void onSnapTo(RPC rpc, SnapTo packet) {}
    public void onStartMeeting(RPC rpc, StartMeeting packet) {}
    public void onSyncSettings(RPC rpc, SyncSettings packet) {}
    
    public void onUpdateGameData(RPC rpc, UpdateGameData packet) {
        Scene scene = client.getScene();
        for(PlayerInfo info : packet.getPlayers()) {
            PlayerDB db = (PlayerDB)scene.getPlayer(info.playerId);
            if(db == null) continue;
            
            db.name = info.name;
            db.colorId = info.colorId;
            db.hatId = info.hatId;
            db.petId = info.petId;
            db.skinId = info.skinId;
            db.setFlags(info.flags);
        }
    }
    
    public void onVotingComplete(RPC rpc, VotingComplete packet) {}
    
    // net
    

    public void onLobbyBehaviour(LobbyBehaviour packet) {
        
    }
    
    public void onNetGameData(NetGameData packet) {
        
    }
    
    public void onVoteBanSystem(VoteBanSystem packet) {
        
    }
    
    public void onPlayerControl(PlayerControl packet) {
        
    }
    
    public void onPlayerPhysics(PlayerPhysics packet) {
        
    }
    
    public void onCustomNetworkTransform(CustomNetworkTransform packet) {
        
    }
}
