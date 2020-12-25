package com.sekwah.mira4j.unity;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.game.GameManager;
import com.sekwah.mira4j.network.packets.net.Component;
import com.sekwah.mira4j.utils.NonNull;
import com.sekwah.mira4j.utils.Nullable;

public interface Scene {
    // Because AmongUs is made in unity each server is treated as a scene
    // and inside a scene we have access to all scripts and objects with
    // methods.
    
    // A lot of the packages used while playing a game uses those references
    // so the easiest way would to have all of them extend a base class unique
    // for each game.
    
    // This would create a sandbox thread that has references stored to the
    // scene that it belongs to.
    
    /**
     * Returns a reference to the global game manager.
     * @return a reference to the global game manager
     */
    @NonNull
    GameManager getGlobalManager();
    
    /**
     * Returns the game id for this scene.
     * @return the game id for this scene
     */
    int getGameId();
    
    /**
     * Returns all the players inside this scene.
     * @return all the players inside this scene
     */
    @NonNull
    Player[] getPlayers();
    
    /**
     * Returns the amount of players inside this scene.
     * @return the amount of players inside this scene
     */
    int getNumPlayers();
    
    /**
     * Add a player to this scene.
     */
    void addPlayer(@NonNull Player player);
    
    /**
     * Remove a player from this scene.
     */
    void removePlayer(@NonNull Player player);
    
    /**
     * Returns the player with the specified client id inside this scene.
     * @param id the client id of the player
     * @return the player with the specified client id
     */
    @Nullable
    Player getPlayer(int clientId);
    
    /**
     * Returns the component with the specified <code>netId</code> in this scene.
     * @param netId the id of the component
     * @return the component with the specified <code>netId</code> or <code>null</code>
     */
    @Nullable
    Component getComponent(int netId);
    
    /**
     * Returns the component with the specified <code>netId</code> in this scene.
     * @param <T> the component type
     * @param type the component class
     * @param netId the id of the component
     * @return the component with the specified <code>netId</code> or <code>null</code>
     */
    @Nullable
    <T extends Component> T getComponent(Class<T> type, int netId);
    
    /**
     * Tick all the scene objects in the world ?!
     *
     */
    @Deprecated
    void tick();
}