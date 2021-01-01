package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class StartMeeting implements RPCMessage {
    private int victimPlayerId;
    
    public StartMeeting() {
        
    }
    
    public StartMeeting(int victimPlayerId) {
        this.victimPlayerId = victimPlayerId;
    }
    
    @Override
    public void read(PacketBuf reader) {
        victimPlayerId = reader.readUnsignedByte();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeUnsignedByte(victimPlayerId);
    }
    
    @Override
    public int id() {
        return RPCType.StartMeeting.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onStartMeeting(rpc, this);
    }
    
    public int getVictimPlayerId() {
        return victimPlayerId;
    }
    
    @Override
    public String toString() {
        return String.format("StartMeeting[ victimPlayerId=%d ]", victimPlayerId);
    }
}
