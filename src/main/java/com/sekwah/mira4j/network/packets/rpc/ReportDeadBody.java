package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;

public class ReportDeadBody implements RPCMessage {
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
        return RPCType.ReportDeadBody.getId();
    }
    
    public int getVictimId() {
        return victimId;
    }
    
    @Override
    public String toString() {
        return String.format("ReportDeadBody[ victimId=%d ]", victimId);
    }
}
