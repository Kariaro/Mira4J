package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class ReportDeadBody implements RPCMessage {
    private int victimId;
    
    public ReportDeadBody() {
        
    }
    
    public ReportDeadBody(int victimId) {
        this.victimId = victimId;
    }
    
    @Override
    public void read(PacketBuf reader) {
        victimId = reader.readUnsignedByte();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeUnsignedByte(victimId);
    }
    
    @Override
    public int id() {
        return RPCType.ReportDeadBody.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onReportDeadBody(rpc, this);
    }
    
    public int getVictimId() {
        return victimId;
    }
    
    @Override
    public String toString() {
        return String.format("ReportDeadBody[ victimId=%d ]", victimId);
    }
}
