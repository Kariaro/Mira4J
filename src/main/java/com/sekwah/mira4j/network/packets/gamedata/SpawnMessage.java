package com.sekwah.mira4j.network.packets.gamedata;

import java.util.Arrays;

import com.sekwah.mira4j.Mira4J;
import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.config.SpawnFlag;
import com.sekwah.mira4j.config.SpawnType;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.GameDataType;
import com.sekwah.mira4j.network.Packets.NetType;
import com.sekwah.mira4j.network.decoder.ClientInListener;
import com.sekwah.mira4j.network.packets.net.Component;
import com.sekwah.mira4j.network.packets.net.InnerNet;

public class SpawnMessage implements GameDataMessage {
    private final Scene scene;
    private SpawnType spawnType;
    private int ownerClientId;
    private int spawnFlags;
    private Component[] components;
    
    public SpawnMessage(Scene scene) {
        this.scene = scene;
    }
    
    public SpawnMessage(SpawnType spawnType, int ownerClientId, int spawnFlags, Component... components) {
        this.scene = null;
        this.spawnType = spawnType;
        this.ownerClientId = ownerClientId;
        this.spawnFlags = spawnFlags;
        this.components = components.clone();
    }
    
    @Override
    public void read(PacketBuf reader, boolean isSpawning) {
        spawnType = SpawnType.fromId(reader.readUnsignedPackedInt());
        ownerClientId = reader.readPackedInt();
        spawnFlags = reader.readUnsignedByte();
        
        int length = reader.readUnsignedPackedInt();
        components = new Component[length];
        
        // TODO: Make sure that this does not crash!
        switch (spawnType) {
            case PLAYER_CONTROL: {
                components[0] = InnerNet.read(reader, scene, NetType.PlayerControl.getId(), true);
                components[1] = InnerNet.read(reader, scene, NetType.PlayerPhysics.getId(), true);
                components[2] = InnerNet.read(reader, scene, NetType.CustomNetworkTransform.getId(), true);
                break;
            }
            case LOBBY_BEHAVIOUR: {
                components[0] = InnerNet.read(reader, scene, NetType.LobbyBehaviour.getId(), true);
                break;
            }
            case GAME_DATA: {
                components[0] = InnerNet.read(reader, scene, NetType.NetGameData.getId(), true);
                components[1] = InnerNet.read(reader, scene, NetType.VoteBanSystem.getId(), true);
                break;
            }
            default: {
                Mira4J.LOGGER.info("Missing spawn type '{}'", spawnType);
            }
        }
//        for (int i = 0; i < length; i++) {
//            Component object = InnerNet.read(reader, spawnType.getId(), isSpawning);
//            components[i] = object;
//        }
    }
    
    @Override
    public void write(PacketBuf writer, boolean isSpawning) {
        writer.writeUnsignedPackedInt(spawnType.getId());
        writer.writeUnsignedPackedInt(ownerClientId);
        writer.writeUnsignedByte(spawnFlags);
        writer.writeUnsignedPackedInt(components.length);
        
        for(Component c : components) {
            c.write(writer, isSpawning);
        }
    }
    
    @Override
    public int id() {
        return GameDataType.Spawn.getId();
    }
    
    @Override
    public void forwardPacket(ClientInListener listener) {
        for (Component c : components) {
            c.forwardPacket(listener);
        }
    }
    
    public SpawnType getSpawnType() {
        return spawnType;
    }
    
    public int getOwnerClientId() {
        return ownerClientId;
    }
    
    public int getSpawnFlags() {
        return spawnFlags;
    }
    
    public Component[] getComponents() {
        return components;
    }
    
    public String toString() {
        return String.format("Spawn { spawnType=%s, ownerClientId=%d spawnFlags=%s, components=%s }",
            spawnType,
            ownerClientId,
            SpawnFlag.toString(spawnFlags),
            Arrays.deepToString(components)
        );
    }
}
