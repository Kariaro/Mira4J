package com.sekwah.mira4j.impl.unity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.utils.GameUtils;
import com.sekwah.mira4j.utils.Nullable;

public class GameManager {
    public static final GameManager INSTANCE = new GameManager();
    private static final Runnable LOBBY_COLLECTOR = () -> {
//        Map<Integer, GameLobby> lobbies = GameManager.INSTANCE.lobbies;
//        
//        while(true) {
//            try {
//                Thread.sleep(1000);
//            } catch(InterruptedException e) {
//                break;
//            }
//            
//            long now = System.currentTimeMillis();
//            Iterator<GameLobby> iter = lobbies.values().iterator();
//            while(iter.hasNext()) {
//                GameLobby lobby = iter.next();
//                
//                long ellapsed = now - lobby.getCreationTime();
//                
//                // 10 seconds
//                if(ellapsed > 10000) {
//                    lobby.disconnect();
//                    iter.remove();
//                }
//            }
//        }
    };
    
    public static final void init() {
        Thread collector = new Thread(LOBBY_COLLECTOR);
        collector.setName("Mira4J Lobby Expiration GC");
        collector.start();
    }
    
    private static final AtomicInteger lobby_id_count = new AtomicInteger();
    private static final AtomicInteger session_id_count = new AtomicInteger(2);
    
    // Garbage queue?
    private Map<Integer, GameLobby> lobbies = new HashMap<>();
    private Map<Integer, Player> clients = new HashMap<>();
    
    private GameManager() {
        
    }
    
    public static int nextSessionId() {
        return session_id_count.getAndIncrement();
    }
    
    public GameLobby createNewLobby() {
        int gameId = GameUtils.generateValidGameInt(lobby_id_count.getAndIncrement());
        Scene scene = new SceneDB(gameId);
        
        GameLobby lobby = new GameLobby(scene);
        lobbies.put(gameId, lobby);
        
        return lobby;
    }
    
    public GameLobby createLobby(String value) {
        int gameId = GameUtils.getIntFromGameString(value);
        GameLobby lobby = new GameLobby(new SceneDB(gameId));
        lobbies.put(gameId, lobby);
        return lobby;
    }
    
    public GameLobby getLobby(int gameId) {
        return lobbies.get(gameId);
    }
    
    public void onPlayerDisconnect(PlayerDB player) {
        clients.remove(player.getClientId());
    }
    
    @Nullable
    public Scene getScene(int gameId) {
        GameLobby lobby = getLobby(gameId);
        if (lobby == null) return null;
        return lobby.getScene();
    }
}
