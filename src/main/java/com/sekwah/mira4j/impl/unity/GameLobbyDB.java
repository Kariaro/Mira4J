package com.sekwah.mira4j.impl.unity;

import java.util.List;

import com.sekwah.mira4j.api.GameLobby;
import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.game.GameOptionsData;
import com.sekwah.mira4j.utils.NonNull;
import com.sekwah.mira4j.utils.Nullable;

public class GameLobbyDB implements GameLobby {
    private final Scene scene;
    private final long creationTime;

    private GameOptionsData data;
    private int hostId;
    
    protected GameLobbyDB(int gameId) {
        this.data = new GameOptionsData();
        this.scene = new SceneDB(gameId);
        this.creationTime = System.currentTimeMillis();
    }

    public long getCreationTime() {
        return creationTime;
    }

    @Nullable
    public Player getPlayerByClientId(int clientId) {
        return scene.getPlayerFromClientId(clientId);
    }

    @Nullable
    public Player getPlayerById(int playerId) {
        return scene.getPlayer(playerId);
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
        
        PlayerDB plr = (PlayerDB)player;
        if (plr.lobby == null) {
            plr.lobby = this;
        } else {
            throw new IllegalStateException("A player tried to join multiple lobbys");
        }
    }

    public void removePlayer(Player player) {
        scene.removePlayer(player);
    }

    public void setHost(Player player) {
        this.hostId = player.getClientId();
    }

    @Nullable
    public Player getHost() {
        return scene.getPlayerFromClientId(hostId);
    }

    public int getNumPlayers() {
        return scene.getNumPlayers();
    }

    public List<Player> getPlayers() {
        return scene.getPlayers();
    }

    @NonNull
    public Scene getScene() {
        return scene;
    }

    public void disconnect() {
        
    }
    
    public boolean hasExpired() {
        // TODO: Use a constant value for the expiration time
        return (System.currentTimeMillis() - creationTime) > 10000000; // 1000 seconds
    }
}
