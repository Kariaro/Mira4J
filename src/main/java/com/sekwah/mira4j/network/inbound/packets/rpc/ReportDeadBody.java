package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets;

public class ReportDeadBody implements RPCObject {
    private int victimId;
    
    public ReportDeadBody() {
        
    }
    
    public ReportDeadBody(int victimId) {
        this.victimId = victimId;
    }
    
    public void read(PacketBuf reader) {
        victimId = reader.readUnsignedByte();
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedByte(victimId);
    }
    
    public int id() {
        return Packets.RPCType.ReportDeadBody.getId();
    }
    
    public int getVictimId() {
        return victimId;
    }
    
    @Override
    public String toString() {
        return String.format("ReportDeadBody[ victimId=%d ]", victimId);
    }
}
