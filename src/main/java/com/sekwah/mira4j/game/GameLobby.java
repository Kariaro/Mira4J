package com.sekwah.mira4j.game;

import java.util.Objects;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.utils.NonNull;
import com.sekwah.mira4j.utils.Nullable;

public class GameLobby {
    private final Scene scene;
    private final long creationTime;
    
    private GameOptionsData data;
    private int hostId;
    
    public GameLobby(@NonNull Scene scene) {
        this.data = new GameOptionsData();
        this.scene = Objects.requireNonNull(scene);
        this.creationTime = System.currentTimeMillis();
    }
    
    public long getCreationTime() {
        return creationTime;
    }
    
    @Nullable
    public Player getPlayerById(int clientId) {
        return scene.getPlayer(clientId);
    }
    
    public void setSettings(GameOptionsData data) {
        data.load(data);
    }
    
    @NonNull
    public GameOptionsData getSettings() {
        return data;
    }
    
    public int getGameId() {
        return scene.getGameId();
    }
    
    public void addPlayer(Player player) {
        scene.addPlayer(player);
    }
    
    public void removePlayer(Player player) {
        scene.removePlayer(player);
    }
    
    public void setHost(Player player) {
        this.hostId = player.getClientId();
    }
    
    public Player getHost() {
        return scene.getPlayer(hostId);
    }
    
    public int getNumPlayers() {
        return scene.getNumPlayers();
    }
    
    public Player[] getPlayers() {
        return scene.getPlayers();
    }
    
    @NonNull
    public Scene getScene() {
        return scene;
    }

    public void disconnect() {
        
    }
}
