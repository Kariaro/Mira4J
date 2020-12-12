package com.sekwah.mira4j.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Lobby {
    private List<Player> players;
    private int gameId;
    private int hostId;
    
    public Lobby(int gameId) {
        players = new ArrayList<>();
        this.gameId = gameId;
    }
    
    public Player getPlayerById(int id) {
        Iterator<Player> list = players.iterator();
        while(list.hasNext()) {
            Player player = list.next();
            if(player.getClientId() == id) return player;
        }
        
        return null;
    }
    
    public void addPlayer(Player player) {
        this.players.add(player);
    }
    
    public void setHostId(int id) {
        this.hostId = id;
    }
    
    public int getNumPlayers() {
        return players.size();
    }
    
    public int getGameId() {
        return gameId;
    }
    
    public Player getHost() {
        return getPlayerById(hostId);
    }
    
    public Player[] getPlayers() {
        return players.toArray(new Player[0]);
    }
}
