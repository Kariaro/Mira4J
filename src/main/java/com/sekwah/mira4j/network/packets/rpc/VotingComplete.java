package com.sekwah.mira4j.network.packets.rpc;

import java.util.Arrays;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class VotingComplete implements RPCMessage {
    private byte[] voteStates;
    private int exiledPlayerId;
    private boolean isTie;
    
    public VotingComplete() {
        
    }
    
    public VotingComplete(byte[] voteStates, int exiledPlayerId, boolean isTie) {
        this.voteStates = voteStates;
        this.exiledPlayerId = exiledPlayerId;
        this.isTie = isTie;
    }
    
    @Override
    public void read(PacketBuf reader) {
        int length = reader.readUnsignedPackedInt();
        voteStates = reader.readBytes(length);
        exiledPlayerId = reader.readUnsignedByte();
        isTie = reader.readBoolean();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeUnsignedPackedInt(voteStates.length);
        writer.writeUnsignedByte(exiledPlayerId);
        writer.writeBoolean(isTie);
    }
    
    @Override
    public int id() {
        return RPCType.VotingComplete.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onVotingComplete(rpc, this);
    }
    
    public byte[] getVoteStates() {
        return voteStates;
    }
    
    public int getExiledPlayerId() {
        return exiledPlayerId;
    }
    
    public boolean isTie() {
        return isTie;
    }
    
    @Override
    public String toString() {
        return String.format("VotingComplete[ voteStates=%s, exiledPlayerId=%d, isTie=%s ]", Arrays.toString(voteStates), exiledPlayerId, isTie);
    }
}
