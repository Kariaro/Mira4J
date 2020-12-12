package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.config.Vector2;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets;

public class SnapTo implements RPCObject {
    private Vector2 position;
    private int lastSequenceId;
    
    public SnapTo() {
        
    }
    
    public SnapTo(Vector2 position, int lastSequenceId) {
        this.position = position;
        this.lastSequenceId = lastSequenceId;
    }
    
    public void read(PacketBuf reader) {
        position = reader.readVector2();
        lastSequenceId = reader.readUnsignedShort();
    }
    
    public void write(PacketBuf writer) {
        writer.writeVector2(position);
        writer.writeUnsignedShort(lastSequenceId);
    }
    
    public int id() {
        return Packets.RPCType.SnapTo.getId();
    }
    
    public Vector2 getPosition() {
        return position;
    }
    
    public int getLastSequenceId() {
        return lastSequenceId;
    }
    
    @Override
    public String toString() {
        return String.format("SnapTo[ position=%s, lastSequenceId=%d ]", position, lastSequenceId);
    }
}