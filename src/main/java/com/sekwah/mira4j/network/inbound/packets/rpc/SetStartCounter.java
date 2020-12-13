package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets;

public class SetStartCounter implements RPCMessage {
    private int sequenceId;
    private int timeRemaining;
    
    public SetStartCounter() {
        
    }
    
    public SetStartCounter(int sequenceId, int timeRemaining) {
        this.sequenceId = sequenceId;
        this.timeRemaining = timeRemaining;
    }
    
    public void read(PacketBuf reader) {
        sequenceId = reader.readUnsignedPackedInt();
        timeRemaining = reader.readByte();
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedPackedInt(sequenceId);
        writer.writeByte(timeRemaining);
    }
    
    public int id() {
        return Packets.RPCType.SetStartCounter.getId();
    }
    
    public int getSequenceId() {
        return sequenceId;
    }
    
    public int getTimeRemaining() {
        return timeRemaining;
    }
    
    @Override
    public String toString() {
        return String.format("SetStartCounter[ sequenceId=%d, timeRemaining=%d ]", sequenceId, timeRemaining);
    }
}
