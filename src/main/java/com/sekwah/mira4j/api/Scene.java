package com.sekwah.mira4j.api;

import java.util.List;

import com.sekwah.mira4j.network.packets.net.Component;
import com.sekwah.mira4j.utils.NonNull;
import com.sekwah.mira4j.utils.Nullable;

public interface Scene {
    /**
     * Returns the game id.
     * @return the game id
     */
    int getGameId();
    
    /**
     * Returns a list of players inside this scene.
     * @return a list of players inside this scene
     */
    @NonNull List<Player> getPlayers();
    
    /**
     * Returns a player from a specified playerId.
     * @return a player from a specified playerId
     */
    @Nullable Player getPlayer(int playerId);
    
    /**
     * Returns the amount of players inside this scene.
     * @return the amount of players inside this scene
     */
    int getNumPlayers();
    
    /**
     * Add a player to this scene.
     * @param player the player to add
     */
    void addPlayer(@NonNull Player player);
    
    /**
     * Remove a player from this scene.
     * @param player the player to remove
     */
    void removePlayer(@NonNull Player player);
    
    /**
     * Returns the player with the specified client id inside this scene.
     * @param clientId the client id of the player
     * @return the player with the specified client id or <code>null</code>
     */
    @Nullable Player getPlayerFromClientId(int clientId);
    
    /**
     * Returns the component with the specified <code>netId</code> in this scene.
     * @param netId the id of the component
     * @return the component with the specified <code>netId</code> or <code>null</code>
     */
    @Nullable Component getComponent(int netId);
    
    /**
     * Returns the component with the specified <code>netId</code> in this scene.
     * @param <T> the component type
     * @param type the component class
     * @param netId the id of the component
     * @return the component with the specified <code>netId</code> or <code>null</code>
     */
    @Nullable <T extends Component> T getComponent(Class<T> type, int netId);
    
    /**
     * Add a new component to a player.
     * @param player the player
     * @param component the component
     */
    void addComponent(Player player, @NonNull Component component);
    
    /**
     * Remove a component from a player.
     * @param player the player
     * @param component the component
     */
    void removeComponent(Player player, @NonNull Component component);
    
    /**
     * Tick all the scene objects in the world ?!
     */
    @Deprecated void tick();
}