package com.sekwah.mira4j.network.inbound.packets.net;

import com.sekwah.mira4j.config.Vector2;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.NetType;

public class CustomNetworkTransform implements Component {
    private int netId;
    private int lastSequenceId;
    private Vector2 targetPosition;
    private Vector2 targetVelocity;
    
    @Override
    public void read(PacketBuf reader, boolean isSpawning) {
        netId = reader.readUnsignedPackedInt();
        
        lastSequenceId = reader.readUnsignedShort();
        targetPosition = reader.readVector2();
        targetVelocity = reader.readVector2();
    }

    @Override
    public void write(PacketBuf writer, boolean isSpawning) {
        
    }

    @Override
    public int id() {
        return NetType.CustomNetworkTransform.getId();
    }
    
    public int getNetId() {
        return netId;
    }
    
    public int getLastSequenceId() {
        return lastSequenceId;
    }
    
    public Vector2 getTargetPosition() {
        return targetPosition;
    }
    
    public Vector2 getTargetVelocity() {
        return targetVelocity;
    }
    
    @Override
    public String toString() {
        return String.format("CustomNetworkTransform[ netId=%d, lastSequenceId=%d, targetPosition=%s, targetVelocity=%s ]",
            netId,
            lastSequenceId,
            targetPosition,
            targetVelocity
        );
    }
}
