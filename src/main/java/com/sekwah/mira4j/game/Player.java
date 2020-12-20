package com.sekwah.mira4j.game;

import com.sekwah.mira4j.api.IPlayer;
import com.sekwah.mira4j.config.TaskInfo;

public class Player implements IPlayer {
    private int clientId;
    private String name;
    private boolean isDisconnected;
    private boolean isImpostor;
    private boolean isDead;
    private int colorId;
    private int hatId;
    private int petId;
    private int skinId;
    private TaskInfo[] tasks;
    
    private boolean dirty;
    
    public Player(int clientId) {
        this.clientId = clientId;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
        dirty = true;
    }
    
    @Override
    public int getFlags() {
        return (isDisconnected ? 1:0)
             | (isImpostor ? 2:0)
             | (isDead ? 4:0);
    }
    
    @Override
    public void setFlags(int flags) {
        isDisconnected = (flags & 1) != 0;
        isImpostor = (flags & 2) != 0;
        isDead = (flags & 4) != 0;
        dirty = true;
    }
    
    @Override
    public boolean isDisconnected() {
        return isDisconnected;
    }
    
    @Override
    public boolean isImpostor() {
        return isImpostor;
    }
    
    @Override
    public boolean isDead() {
        return isDead;
    }
    
    @Override
    public int getColorId() {
        return colorId;
    }
    
    @Override
    public int getHatId() {
        return hatId;
    }
    
    @Override
    public int getPetId() {
        return petId;
    }
    
    @Override
    public int getSkinId() {
        return skinId;
    }
    
    @Override
    public void setColorId(int colorId) {
        this.colorId = colorId;
        dirty = true;
    }
    
    @Override
    public void setHatId(int hatId) {
        this.hatId = hatId;
        dirty = true;
    }
    
    @Override
    public void setPetId(int petId) {
        this.petId = petId;
        dirty = true;
    }
    
    @Override
    public void setSkinId(int skinId) {
        this.skinId = skinId;
        dirty = true;
    }
    
    @Override
    public boolean isDirty() {
        return dirty;
    }
    
    @Override
    public TaskInfo[] getTasks() {
        return tasks;
    }
    
    @Override
    public int hashCode() {
        return clientId;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Player)) return false;
        return hashCode() == obj.hashCode();
    }
    
    @Override
    public int getClientId() {
        return clientId;
    }
    
    @Override
    public String toString() {
        return String.format("Player '%s' { clientId=%d }", name, clientId);
    }
}
