package com.sekwah.mira4j.api;

import com.sekwah.mira4j.config.TaskInfo;
import com.sekwah.mira4j.utils.NonNull;

public interface Player {
    /**
     * Returns the name of this player or an empty string if the name is undefined.
     * @return the name of this player
     */
    @NonNull
    String getName();
    
    int getFlags();
    int getColorId();
    int getHatId();
    int getPetId();
    int getSkinId();
    int getClientId();
    
    TaskInfo[] getTasks();
    
    /**
     * Set the name of this player.
     * @param name a name
     */
    void setName(@NonNull String name);
    
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
