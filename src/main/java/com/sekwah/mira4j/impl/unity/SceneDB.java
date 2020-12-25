package com.sekwah.mira4j.impl.unity;

import java.util.*;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.game.GameManager;
import com.sekwah.mira4j.network.packets.net.Component;
import com.sekwah.mira4j.unity.Scene;
import com.sekwah.mira4j.unity.SceneObject;
import com.sekwah.mira4j.utils.GameUtils;

public class SceneDB implements Scene {
    private final GameManager manager;
    private final int gameId;
    // Concurent hash map!?
    private final Map<Integer, SceneObject> objects = new HashMap<>();
    private final List<Player> players = new ArrayList<>();
    
    public SceneDB(GameManager manager, int gameId) {
        this.manager = manager;
        this.gameId = gameId;
    }
    
    @Override
    public GameManager getGlobalManager() {
        return manager;
    }
    
    @Override
    public int getGameId() {
        return gameId;
    }

    @Override
    public Component getComponent(int netId) {
        Iterator<Player> iter = players.iterator();
        while (iter.hasNext()) {
            Component comp = ((PlayerDB)iter.next()).getComponent(netId);
            if(comp != null) return comp;
        }
        
        return null;
    }

    @Override
    public <T extends Component> T getComponent(Class<T> type, int netId) {
        if (type == null) throw new IllegalArgumentException();
        
        SceneObject object = objects.get(netId);
        if (type.isInstance(object)) {
            return type.cast(object);
        }
        
        return null;
    }

    @Override
    public Player getPlayer(int clientId) {
        Iterator<Player> iter = players.iterator();
        while (iter.hasNext()) {
            Player player = iter.next();
            if (player.getClientId() == clientId) return player;
        }
        
        return null;
    }
    
    @Override
    public Player[] getPlayers() {
        return players.toArray(new Player[0]);
    }
    
    @Override
    public int getNumPlayers() {
        return players.size();
    }
    
    @Override
    public void addPlayer(Player player) {
        players.add(player);
    }
    
    @Override
    public void removePlayer(Player player) {
        players.remove(player);
    }
    
    @Override
    @Deprecated
    public void tick() {
        // TODO: Should we tick stuff here?
    }
    
    @Override
    public String toString() {
        return String.format("Scene{ game=\"%s\" }", GameUtils.getStringFromGameId(gameId));
    }
}
