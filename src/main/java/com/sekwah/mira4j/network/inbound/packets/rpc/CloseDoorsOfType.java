package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets;

public class CloseDoorsOfType implements RPCObject {
    private int systemId;
    
    public CloseDoorsOfType() {
        
    }
    
    public CloseDoorsOfType(int systemId) {
        this.systemId = systemId;
    }
    
    public void read(PacketBuf reader) {
        systemId = reader.readUnsignedByte();
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedByte(systemId);
    }
    
    public int id() {
        return Packets.RPCType.CloseDoorsOfType.getId();
    }
    
    public int getSystemId() {
        return systemId;
    }
    
    @Override
    public String toString() {
        return String.format("CloseDoorsOfType[ systemId=%d ]", systemId);
    }
}
