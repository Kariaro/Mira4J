package com.sekwah.mira4j.network.packets.net;

import com.sekwah.mira4j.config.Vector2;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.NetType;

public class CustomNetworkTransform implements Component {
    private int netId;
    private int lastSequenceId;
    private Vector2 targetPosition;
    private Vector2 targetVelocity;
    
    public CustomNetworkTransform() {
        
    }
    
    public CustomNetworkTransform(int netId) {
        this.netId = netId;
    }
    
    @Override
    public void read(PacketBuf reader, boolean isSpawning) {
        netId = reader.readUnsignedPackedInt();
        
        if (isSpawning) {
            reader = reader.readMessage();
        }
        
        lastSequenceId = reader.readUnsignedShort();
        targetPosition = reader.readVector2();
        targetVelocity = reader.readVector2();
        
        if (isSpawning) {
           reader.release(); 
        }
    }

    @Override
    public void write(PacketBuf writer, boolean isSpawning) {
        writer.writeUnsignedPackedInt(netId);
        
        if (isSpawning) {
            writer.startMessage(0);
        }
        
        writer.writeUnsignedShort(lastSequenceId);
        writer.writeVector2(targetPosition);
        writer.writeVector2(targetVelocity);
        
        if (isSpawning) {
            writer.endMessage();
        }
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
