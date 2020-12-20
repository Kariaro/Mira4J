package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;

public class ExitVent implements RPCMessage {
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
        return RPCType.ExitVent.getId();
    }
    
    public int getVentId() {
        return ventId;
    }
    
    @Override
    public String toString() {
        return String.format("ExitVent[ ventId=%d ]", ventId);
    }
}
