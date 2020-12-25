package com.sekwah.mira4j.impl.unity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.config.TaskInfo;
import com.sekwah.mira4j.network.packets.net.Component;
import com.sekwah.mira4j.utils.Nullable;

public class PlayerDB implements Player {
    private int clientId;
    
    private String name = "";
    private boolean isDisconnected;
    private boolean isImpostor;
    private boolean isDead;
    
    private int colorId;
    private int hatId;
    private int petId;
    private int skinId;
    
    // This is used ingame
    private TaskInfo[] tasks;
    
    private Map<Integer, Component> components = new HashMap<>();
    
    // If this player needs to update
    private boolean dirty;
    
    public PlayerDB(int clientId) {
        this.clientId = clientId;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public void addComponent(Component comp) {
        if (comp == null) return;
        components.put(comp.getNetId(), comp);
    }
    
    @Nullable
    public <T extends Component> T getComponent(Class<T> type) {
        for (Component comp : components.values()) {
            if (type.isInstance(comp)) {
                return type.cast(comp);
            }
        }
        
        return null;
    }
    
    @Nullable
    public Component getComponent(int netId) {
        return components.get(netId);
    }
    
    @Override
    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
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
        if (!(obj instanceof PlayerDB)) return false;
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
