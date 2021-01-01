package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class CastVote implements RPCMessage {
    private int votingPlayerId;
    private int suspectPlayerId;
    
    public CastVote() {
        
    }
    
    public CastVote(int votingPlayerId, int suspectPlayerId) {
        this.votingPlayerId = votingPlayerId;
        this.suspectPlayerId = suspectPlayerId;
    }
    
    @Override
    public void read(PacketBuf reader) {
        votingPlayerId = reader.readUnsignedByte();
        suspectPlayerId = reader.readUnsignedByte();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeUnsignedByte(votingPlayerId);
        writer.writeUnsignedByte(suspectPlayerId);
    }
    
    @Override
    public int id() {
        return RPCType.CastVote.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onCastVote(rpc, this);
    }
    
    public int getVotingPlayerId() {
        return votingPlayerId;
    }
    
    public int getSuspectPlayerId() {
        return suspectPlayerId;
    }
    
    @Override
    public String toString() {
        return String.format("CastVote[ votingPlayerId=%d, suspectPlayerId=%d ]", votingPlayerId, suspectPlayerId);
    }
}
