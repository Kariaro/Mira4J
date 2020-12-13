package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets;

public class CheckName implements RPCMessage {
    private String name;
    
    public CheckName() {
        
    }
    
    public CheckName(String name) {
        this.name = name;
    }
    
    public void read(PacketBuf reader) {
        name = reader.readString();
    }
    
    public void write(PacketBuf writer) {
        writer.writeString(name);
    }
    
    public int id() {
        return Packets.RPCType.CheckName.getId();
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return String.format("CheckName[ name=\"%s\" ]", name);
    }
}
