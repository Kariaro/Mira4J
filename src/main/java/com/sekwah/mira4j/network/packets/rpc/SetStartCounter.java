package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class SetStartCounter implements RPCMessage {
    private int sequenceId;
    private int timeRemaining;
    
    public SetStartCounter() {
        
    }
    
    public SetStartCounter(int sequenceId, int timeRemaining) {
        this.sequenceId = sequenceId;
        this.timeRemaining = timeRemaining;
    }
    
    @Override
    public void read(PacketBuf reader) {
        sequenceId = reader.readUnsignedPackedInt();
        timeRemaining = reader.readByte();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeUnsignedPackedInt(sequenceId);
        writer.writeByte(timeRemaining);
    }
    
    @Override
    public int id() {
        return RPCType.SetStartCounter.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onSetStartCounter(rpc, this);
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
