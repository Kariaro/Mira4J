package com.sekwah.mira4j.impl.unity;

import java.util.HashMap;
import java.util.Map;

import com.sekwah.mira4j.game.GameManager;
import com.sekwah.mira4j.game.Player;
import com.sekwah.mira4j.network.packets.net.Component;
import com.sekwah.mira4j.unity.Scene;
import com.sekwah.mira4j.unity.SceneObject;
import com.sekwah.mira4j.utils.GameUtils;

public class SceneDB implements Scene {
    private final GameManager manager;
    private final int gameId;
    // Concurent hash map!?
    private final Map<Integer, SceneObject> objects = new HashMap<>();
    private final Player[] players = new Player[11]; // MaxLength + 1
    
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
        return (Component)objects.get(netId);
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
    public Player getPlayer(int playerId) {
        if(playerId < 0 || playerId > 10) return null; // Bad
        return players[playerId];
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
