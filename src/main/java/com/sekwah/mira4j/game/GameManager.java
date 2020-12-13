package com.sekwah.mira4j.game;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.sekwah.mira4j.utils.GameUtils;

public class GameManager {
    private final AtomicInteger lobby_id = new AtomicInteger();
    private final AtomicInteger player_id = new AtomicInteger(2);
    private Map<Integer, GameLobby> lobbies = new HashMap<>();
    private Map<Integer, Player> players = new HashMap<>();
    
    public GameManager() {
        
    }
    
    public GameLobby createNewLobby() {
        int id = lobby_id.getAndIncrement();
        
        String gameCode = GameUtils.generateGameString(id);
        int gameId = GameUtils.getIntFromGameString(gameCode);
        GameLobby lobby = new GameLobby(gameId);
        lobbies.put(gameId, lobby);
        
        return lobby;
    }
    
    public GameLobby createLobby(String value) {
        int gameId = GameUtils.getIntFromGameString(value);
        GameLobby lobby = new GameLobby(gameId);
        lobbies.put(gameId, lobby);
        return lobby;
    }
    
    public GameLobby getLobby(int gameId) {
        return lobbies.get(gameId);
    }

    public Player newPlayer() {
        int id = player_id.getAndIncrement();
        Player player = new Player(id);
        players.put(id, player);
        return player;
    }
    
    public Player getPlayer(int clientId) {
        return players.get(clientId);
    }
    
    public void onPlayerDisconnect(Player player) {
        players.remove(player.getClientId());
    }
}
