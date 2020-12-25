package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.utils.NonNull;

public class AddVote implements RPCMessage {
    private int votingClientId;
    private int targetClientId;
    
    public AddVote() {
        
    }
    
    public AddVote(int votingClientId, int targetClientId) {
        this.votingClientId = votingClientId;
        this.targetClientId = targetClientId;
    }
    
    public void read(PacketBuf reader) {
        votingClientId = reader.readInt();
        targetClientId = reader.readInt();
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedInt(votingClientId);
        writer.writeUnsignedInt(targetClientId);
    }
    
    public int id() {
        return RPCType.AddVote.getId();
    }
    
    public int getVotingClientId() {
        return votingClientId;
    }
    
    public int getTargetClientId() {
        return targetClientId;
    }
    
    @Override
    public String toString() {
        return String.format("AddVote[ votingClientId=%s, targetClientId=%d ]", votingClientId, targetClientId);
    }
    
    public static AddVote of(@NonNull Player voter, @NonNull Player target) {
        return new AddVote(voter.getClientId(), target.getClientId());
    }
}
