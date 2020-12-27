package com.sekwah.mira4j.impl.unity;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.sekwah.mira4j.api.GameLobby;
import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.utils.GameUtils;
import com.sekwah.mira4j.utils.Nullable;

public final class GameManager {
    private GameManager() {
        
    }
    
    private static final Runnable LOBBY_COLLECTOR = () -> {
        Map<Integer, GameLobby> lobbies = GameManager.lobbies;
        while(true) {
            if(Thread.interrupted()) break;
            
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                break;
            }
            
            Iterator<GameLobby> iter = lobbies.values().iterator();
            while(iter.hasNext()) {
                GameLobby lobby = iter.next();
                
                // 10 seconds
                if(lobby.hasExpired()) {
                    //lobby.disconnect();
                    iter.remove();
                }
            }
        }
    };
    
    public static final void init() {
        Thread collector = new Thread(LOBBY_COLLECTOR);
        collector.setName("Mira4J Lobby Expiration GC");
        collector.setDaemon(true);
        collector.start();
    }
    
    private static final AtomicInteger session_id_count = new AtomicInteger(2);
    private static final AtomicInteger lobby_id_count = new AtomicInteger();
    
    private static Map<Integer, GameLobby> lobbies = new ConcurrentHashMap<>();
    
    
    public static int nextSessionId() {
        return session_id_count.getAndIncrement();
    }
    
    public static GameLobby createNewLobby() {
        int gameId = GameUtils.generateValidGameInt(lobby_id_count.getAndIncrement());
        GameLobby lobby = new GameLobbyDB(gameId);
        lobbies.put(gameId, lobby);
        return lobby;
    }
    
    public static GameLobby createLobby(String value) {
        int gameId = GameUtils.getIntFromGameString(value);
        GameLobby lobby = new GameLobbyDB(gameId);
        lobbies.put(gameId, lobby);
        return lobby;
    }
    
    public static GameLobby getLobby(int gameId) {
        return lobbies.get(gameId);
    }
    
    @Nullable
    public static Scene getScene(int gameId) {
        GameLobby lobby = getLobby(gameId);
        if (lobby == null) return null;
        return lobby.getScene();
    }
}
