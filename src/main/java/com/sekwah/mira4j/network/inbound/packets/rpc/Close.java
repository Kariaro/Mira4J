package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets;

public class Close implements RPCMessage {
    
    public void read(PacketBuf reader) {
        
    }
    
    public void write(PacketBuf writer) {
        
    }
    
    public int id() {
        return Packets.RPCType.Close.getId();
    }
    
    @Override
    public String toString() {
        return "Close[]";
    }
}
