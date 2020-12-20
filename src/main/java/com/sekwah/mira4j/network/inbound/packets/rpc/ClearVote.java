package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;

public class ClearVote implements RPCMessage {
    
    public void read(PacketBuf reader) {
        
    }
    
    public void write(PacketBuf writer) {
        
    }
    
    public int id() {
        return RPCType.ClearVote.getId();
    }
    
    @Override
    public String toString() {
        return "ClearVote[]";
    }
}
