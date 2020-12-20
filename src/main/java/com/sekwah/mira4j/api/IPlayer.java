package com.sekwah.mira4j.api;

import com.sekwah.mira4j.config.TaskInfo;

public interface IPlayer {
    String getName();
    int getFlags();
    int getColorId();
    int getHatId();
    int getPetId();
    int getSkinId();
    int getClientId();
    
    TaskInfo[] getTasks();
    
    void setName(String name);
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
