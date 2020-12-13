package com.sekwah.mira4j.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameLobby {
    private List<Player> players;
    private GameOptionsData data;
    private int gameId;
    private Player host;
    
    public GameLobby(int gameId) {
        this.gameId = gameId;
        players = new ArrayList<>();
        data = new GameOptionsData();
    }
    
    public Player getPlayerById(int id) {
        Iterator<Player> list = players.iterator();
        while (list.hasNext()) {
            Player player = list.next();
            if (player.getClientId() == id) return player;
        }
        
        return null;
    }
    
    public void setSettings(GameOptionsData data) {
        data.load(data);
    }
    
    public GameOptionsData getSettings() {
        return data;
    }
    
    public int getGameId() {
        return gameId;
    }
    
    public void addPlayer(Player player) {
        this.players.add(player);
    }
    
    public void setHost(Player player) {
        this.host = player;
    }
    
    public Player getHost() {
        return host;
    }
    
    public int getNumPlayers() {
        return players.size();
    }
    
    public Player[] getPlayers() {
        return players.toArray(new Player[0]);
    }
}
