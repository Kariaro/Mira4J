package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;

public class EnterVent implements RPCMessage {
    private int ventId;
    
    public EnterVent() {
        
    }
    
    public EnterVent(int ventId) {
        this.ventId = ventId;
    }
    
    public void read(PacketBuf reader) {
        ventId = reader.readUnsignedPackedInt();
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedPackedInt(ventId);
    }
    
    public int id() {
        return RPCType.EnterVent.getId();
    }
    
    public int getVentId() {
        return ventId;
    }
    
    @Override
    public String toString() {
        return String.format("EnterVent[ ventId=%d ]", ventId);
    }
}