package com.sekwah.mira4j.unity;

import com.sekwah.mira4j.game.GameManager;
import com.sekwah.mira4j.game.Player;
import com.sekwah.mira4j.network.inbound.packets.net.Component;

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
     * Return a reference to the global game manager.
     * @return a reference to the global game manager
     */
    public GameManager getGlobalManager();
    
    /**
     * Return the component with the specified <code>netId</code> in this scene.
     * @param netId the id of the component
     * @return the component with the specified <code>netId</code> or <code>null</code>
     */
    public Component getComponent(int netId);
    
    /**
     * Return the component with the specified <code>netId</code> in this scene.
     * @param <T> the component type
     * @param type the component class
     * @param netId the id of the component
     * @return the component with the specified <code>netId</code> or <code>null</code>
     */
    public <T extends Component> T getComponent(Class<T> type, int netId);
    
    /**
     * Return the player with the specified <code>playerId</code> in this scene.
     * @param playerId the scene id of the player
     * @return the player with the specified <code>playerId</code> or <code>null</code>
     */
    public Player getPlayer(int playerId);
}
