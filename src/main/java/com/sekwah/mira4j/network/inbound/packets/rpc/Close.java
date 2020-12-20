package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;

public class Close implements RPCMessage {
    
    public void read(PacketBuf reader) {
        
    }
    
    public void write(PacketBuf writer) {
        
    }
    
    public int id() {
        return RPCType.Close.getId();
    }
    
    @Override
    public String toString() {
        return "Close[]";
    }
}
