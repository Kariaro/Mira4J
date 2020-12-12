package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets;

public class CastVote implements RPCObject {
    private int votingPlayerId;
    private int suspectPlayerId;
    
    public CastVote() {
        
    }
    
    public CastVote(int votingPlayerId, int suspectPlayerId) {
        this.votingPlayerId = votingPlayerId;
        this.suspectPlayerId = suspectPlayerId;
    }
    
    public void read(PacketBuf reader) {
        votingPlayerId = reader.readUnsignedByte();
        suspectPlayerId = reader.readUnsignedByte();
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedByte(votingPlayerId);
        writer.writeUnsignedByte(suspectPlayerId);
    }
    
    public int id() {
        return Packets.RPCType.CastVote.getId();
    }
    
    public int getVotingPlayerId() {
        return votingPlayerId;
    }
    
    public int getSuspectPlayerId() {
        return suspectPlayerId;
    }
    
    @Override
    public String toString() {
        return String.format("CastVote[ votingPlayerId=%s, suspectPlayerId=%d ]", votingPlayerId, suspectPlayerId);
    }
}
