package com.sekwah.mira4j.network.inbound.packets.gamedata;

import java.util.Arrays;

import com.sekwah.mira4j.config.SpawnFlag;
import com.sekwah.mira4j.config.SpawnType;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.GameDataType;
import com.sekwah.mira4j.network.Packets.NetType;
import com.sekwah.mira4j.network.inbound.packets.net.*;
import com.sekwah.mira4j.unity.Scene;

public class SpawnData implements GameDataMessage {
    private SpawnType spawnType;
    private int ownerId;
    private int spawnFlags;
    private Component[] components;
    
    public SpawnData() {
        
    }
    
    static {
        Scene gdf = null;
        Scene scene = gdf;
        @SuppressWarnings("null")
        PlayerPhysics c = scene.getComponent(PlayerPhysics.class, 1);
        System.out.println(c);
    }
    
    @Override
    public void read(PacketBuf reader, boolean isSpawning) {
        spawnType = SpawnType.fromId(reader.readUnsignedPackedInt());
        ownerId = reader.readPackedInt();
        spawnFlags = reader.readUnsignedByte();
        
        int length = reader.readUnsignedPackedInt();
        components = new Component[length];
        
        // This is always true I hope
        if(spawnType == SpawnType.PLAYER_CONTROL) {
            components[0] = InnerNet.read(reader, NetType.PlayerControl.getId(), isSpawning);
            components[1] = InnerNet.read(reader, NetType.PlayerPhysics.getId(), isSpawning);
            components[2] = InnerNet.read(reader, NetType.CustomNetworkTransform.getId(), isSpawning);
        }
//        for(int i = 0; i < length; i++) {
//            Component object = InnerNet.read(reader, spawnType.getId(), isSpawning);
//            components[i] = object;
//        }
    }
    
    @Override
    public void write(PacketBuf writer, boolean isSpawning) {
        
    }
    
    @Override
    public int id() {
        return GameDataType.Spawn.getId();
    }
    
    public SpawnType getSpawnType() {
        return spawnType;
    }
    
    public int getOwnerId() {
        return ownerId;
    }
    
    public int getSpawnFlags() {
        return spawnFlags;
    }
    
    public Component[] getComponents() {
        return components;
    }
    
    public String toString() {
        return String.format("Spawn { spawnType=%s, ownerId=%d spawnFlags=%s, components=%s }",
            spawnType,
            ownerId,
            SpawnFlag.toString(spawnFlags),
            Arrays.deepToString(components)
        );
    }
}
