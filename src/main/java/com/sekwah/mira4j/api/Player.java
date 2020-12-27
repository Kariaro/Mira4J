package com.sekwah.mira4j.api;

import com.sekwah.mira4j.config.TaskInfo;
import com.sekwah.mira4j.network.packets.net.Component;
import com.sekwah.mira4j.utils.NonNull;
import com.sekwah.mira4j.utils.Nullable;

public interface Player {
    /**
     * Returns the name of this player or an empty string if the name is undefined.
     * @return the name of this player
     */
    @NonNull String getName();
    
    /**
     * Returns the client id of this player.
     * @return the client id of this player
     */
    int getClientId();
    
    /**
     * Returns the lobby that this player is connected to.
     * @return the lobby that this player is connected to
     */
    @NonNull GameLobby getLobby();
    
    /**
     * Returns the scene this player belongs to.
     * @return the scene this player belongs to
     */
    @NonNull Scene getScene();
    
    /**
     * Returns the component with the specified <code>type</code> inside this player.
     * @param <T> the component type
     * @param type the component class
     * @return the component with the specified <code>type</code
     */
    @Nullable <T extends Component> T getComponent(Class<T> type);
    
    /**
     * Returns the color id of this player.
     * @return the color id of this player
     */
    int getColorId();
    
    /**
     * Returns the hat id of this player.
     * @return the hat id of this player
     */
    int getHatId();
    
    /**
     * Returns the pet id of this player.
     * @return the pet id of this player
     */
    int getPetId();
    
    /**
     * Returns the skin id of this player.
     * @return the skin id of this player
     */
    int getSkinId();
    
    TaskInfo[] getTasks();
    
    /**
     * Set the name of this player.
     * @param name the new name of this player
     */
    void setName(@NonNull String name);

    int getFlags();
    void setFlags(int flags);
    void setColorId(int colorId);
    void setHatId(int hatId);
    void setPetId(int petId);
    void setSkinId(int skinId);
    
    boolean isDisconnected();
    boolean isImpostor();
    boolean isDead();
    boolean isDirty();
}
