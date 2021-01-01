package com.sekwah.mira4j.impl.unity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.sekwah.mira4j.api.GameLobby;
import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.config.TaskInfo;
import com.sekwah.mira4j.config.Vector2;
import com.sekwah.mira4j.network.ClientConnection;
import com.sekwah.mira4j.network.packets.hazel.GameData;
import com.sekwah.mira4j.network.packets.net.Component;
import com.sekwah.mira4j.network.packets.net.CustomNetworkTransform;
import com.sekwah.mira4j.network.packets.net.PlayerControl;
import com.sekwah.mira4j.network.packets.rpc.*;
import com.sekwah.mira4j.utils.NonNull;
import com.sekwah.mira4j.utils.Nullable;

public class PlayerDB implements Player {
    private final ClientConnection connection;
    
    /**
     * This is the players clientId and is always constant during a session.
     */
    private final int clientId;
    protected GameLobby lobby;
    
    public String name = "";
    public boolean isDisconnected;
    public boolean isImpostor;
    public boolean isDead;
    
    public int playerId = -1;
    public int colorId;
    public int hatId;
    public int petId;
    public int skinId;
    
    private TaskInfo[] tasks = new TaskInfo[0];
    
    private Map<Integer, Component> comps = new HashMap<>();
    private boolean dirty = true;
    
    public PlayerDB(ClientConnection connection, int clientId) {
        this.connection = connection;
        this.clientId = clientId;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public int getClientId() {
        return clientId;
    }
    
    @Override
    public GameLobby getLobby() {
        return lobby;
    }
    
    @Override
    public Scene getScene() {
        return lobby.getScene();
    }
    
    @Override
    public int getPlayerId() {
        return playerId;
    }
    
    @Override
    public void setName(String name) {
        if(name == null) throw new NullPointerException();
        
        PlayerControl control = getComponent(PlayerControl.class);
        if(control != null) {
            this.name = name;
            connection.sendReliablePacket(GameData.of(lobby, new RPC(control.getNetId(), new SetName(name))));
        }
        
        dirty = true;
    }
    
    @Override
    public void sendChat(String message) {
        if(message == null) throw new NullPointerException();
        
        PlayerControl control = getComponent(PlayerControl.class);
        if(control != null) {
            connection.sendReliablePacket(GameData.of(lobby, new RPC(control.getNetId(), SendChat.of(message))));
        }
    }
    
    @Override
    public Vector2 getLocation() {
        CustomNetworkTransform cnt = getComponent(CustomNetworkTransform.class);
        if(cnt != null) {
            return cnt.getTargetPosition().clone();
        }
        
        return new Vector2(0, 0);
    }
    
    @Override
    public Vector2 getVelocity() {
        CustomNetworkTransform cnt = getComponent(CustomNetworkTransform.class);
        if(cnt != null) {
            return cnt.getTargetVelocity().clone();
        }
        
        return new Vector2(0, 0);
    }
    
    @Override
    public void setLocation(Vector2 location) {
        if(location == null) throw new NullPointerException();
        
        CustomNetworkTransform cnt = getComponent(CustomNetworkTransform.class);
        if(cnt != null) {
            GameData pck = GameData.of(lobby, new RPC(cnt.getNetId(), new SnapTo(location, cnt.getLastSequenceId() + 1)));
            cnt.targetPosition.set(location);
            connection.sendReliablePacket(pck);
        }
    }
    
    @Override
    public void setVelocity(Vector2 velocity) {
        throw new UnsupportedOperationException("setVelocity() is not implemented");
    }
    
    @Override
    public int getColorId() {
        return colorId;
    }
    
    @Override
    public void setColorId(int colorId) {
        PlayerControl control = getComponent(PlayerControl.class);
        if(control != null) {
            this.colorId = colorId;
            connection.sendReliablePacket(GameData.of(lobby, new RPC(control.getNetId(), new SetColor(colorId))));
        }
        dirty = true;
    }
    
    @Override
    public int getHatId() {
        return hatId;
    }
    
    @Override
    public void setHatId(int hatId) {
        PlayerControl control = getComponent(PlayerControl.class);
        if(control != null) {
            this.hatId = hatId;
            connection.sendReliablePacket(GameData.of(lobby, new RPC(control.getNetId(), new SetHat(hatId))));
        }
        
        dirty = true;
    }
    
    @Override
    public int getPetId() {
        return petId;
    }
    
    @Override
    public void setPetId(int petId) {
        PlayerControl control = getComponent(PlayerControl.class);
        if(control != null) {
            this.petId = petId;
            connection.sendReliablePacket(GameData.of(lobby, new RPC(control.getNetId(), new SetPet(petId))));
        }
        
        dirty = true;
    }
    
    @Override
    public int getSkinId() {
        return skinId;
    }
    
    @Override
    public void setSkinId(int skinId) {
        PlayerControl control = getComponent(PlayerControl.class);
        if(control != null) {
            this.skinId = skinId;
            connection.sendReliablePacket(GameData.of(lobby, new RPC(control.getNetId(), new SetSkin(skinId))));
        }
        
        dirty = true;
    }
    
    @Override
    public boolean isDirty() {
        return dirty;
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
    public TaskInfo[] getTasks() {
        return tasks;
    }
    
    @Override
    public void setTasks(@NonNull TaskInfo... tasks) {
        this.tasks = Objects.requireNonNull(tasks);
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
    
    protected void addComponent(Component comp) {
        if (comp == null) return;
        comps.put(comp.getNetId(), comp);
    }
    
    @Nullable
    public <T extends Component> T getComponent(Class<T> type) {
        for (Component comp : comps.values()) {
            if (type.isInstance(comp)) {
                return type.cast(comp);
            }
        }
        
        return null;
    }
    
    @Nullable
    public Component getComponent(int netId) {
        return comps.get(netId);
    }
    
    @Override
    public String toString() {
        return String.format("Player '%s' clientId=%d", name, clientId);
    }

    public void setDirty(boolean b) {
        dirty = false;
    }
}
