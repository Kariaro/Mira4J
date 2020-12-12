package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets;

public class ExitVent implements RPCObject {
    private int ventId;
    
    public ExitVent() {
        
    }
    
    public ExitVent(int ventId) {
        this.ventId = ventId;
    }
    
    public void read(PacketBuf reader) {
        ventId = reader.readUnsignedPackedInt();
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedPackedInt(ventId);
    }
    
    public int id() {
        return Packets.RPCType.ExitVent.getId();
    }
    
    public int getVentId() {
        return ventId;
    }
    
    @Override
    public String toString() {
        return String.format("ExitVent[ ventId=%d ]", ventId);
    }
}
