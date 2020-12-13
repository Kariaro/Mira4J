package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets;

public class SetName implements RPCMessage {
    private String name;
    
    public SetName() {
        
    }
    
    public SetName(String name) {
        this.name = name;
    }
    
    public void read(PacketBuf reader) {
        name = reader.readString();
    }
    
    public void write(PacketBuf writer) {
        writer.writeString(name);
    }
    
    public int id() {
        return Packets.RPCType.SetName.getId();
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return String.format("SetName[ name=\"%s\" ]", name);
    }
}
