package com.sekwah.mira4j.network.packets.net;

import com.sekwah.mira4j.config.Vector2;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.NetType;
import com.sekwah.mira4j.network.decoder.NetListener;

public class CustomNetworkTransform extends ComponentDB {
    public Vector2 targetPosition = new Vector2(0, 0);
    public Vector2 targetVelocity = new Vector2(-0.00061f, -0.00061f);
    public int lastSequenceId = 1;
    
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
            writer.startMessage(1);
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
    
    @Override
    public void forwardPacket(NetListener listener) {
        
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
