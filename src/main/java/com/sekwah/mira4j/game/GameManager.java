package com.sekwah.mira4j.game;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.impl.unity.PlayerDB;
import com.sekwah.mira4j.impl.unity.SceneDB;
import com.sekwah.mira4j.unity.Scene;
import com.sekwah.mira4j.utils.GameUtils;
import com.sekwah.mira4j.utils.Nullable;

public class GameManager {
    public static final GameManager INSTANCE = new GameManager();
    
    private static final AtomicInteger lobby_id_count = new AtomicInteger();
    private static final AtomicInteger client_id_count = new AtomicInteger(232);
    
    // Garbage queue?
    private Map<Integer, GameLobby> lobbies = new HashMap<>();
    private Map<Integer, Player> clients = new HashMap<>();
    
    private GameManager() {
        
    }
    
    public GameLobby createNewLobby() {
        int gameId = GameUtils.generateValidGameInt(lobby_id_count.getAndIncrement());
        Scene scene = new SceneDB(this, gameId);
        
        GameLobby lobby = new GameLobby(scene);
        lobbies.put(gameId, lobby);
        
        return lobby;
    }
    
    public GameLobby createLobby(String value) {
        int gameId = GameUtils.getIntFromGameString(value);
        GameLobby lobby = new GameLobby(new SceneDB(this, gameId));
        lobbies.put(gameId, lobby);
        return lobby;
    }
    
    public GameLobby getLobby(int gameId) {
        return lobbies.get(gameId);
    }

    public Player newPlayer() {
        int id = client_id_count.getAndIncrement();
        Player player = new PlayerDB(id);
        clients.put(id, player);
        return player;
    }
    
    public Player getPlayer(int clientId) {
        return clients.get(clientId);
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
