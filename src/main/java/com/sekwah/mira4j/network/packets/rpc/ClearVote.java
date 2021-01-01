package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class ClearVote implements RPCMessage {
    
    @Override
    public void read(PacketBuf reader) {
        
    }
    
    @Override
    public void write(PacketBuf writer) {
        
    }
    
    @Override
    public int id() {
        return RPCType.ClearVote.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onClearVote(rpc, this);
    }
    
    @Override
    public String toString() {
        return "ClearVote[]";
    }
}
