package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets;

public class StartMeeting implements RPCMessage {
    private int victimPlayerId;
    
    public StartMeeting() {
        
    }
    
    public StartMeeting(int victimPlayerId) {
        this.victimPlayerId = victimPlayerId;
    }
    
    public void read(PacketBuf reader) {
        victimPlayerId = reader.readUnsignedByte();
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedByte(victimPlayerId);
    }
    
    public int id() {
        return Packets.RPCType.StartMeeting.getId();
    }
    
    public int getVictimPlayerId() {
        return victimPlayerId;
    }
    
    @Override
    public String toString() {
        return String.format("StartMeeting[ victimPlayerId=%d ]", victimPlayerId);
    }
}
