package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class SetName implements RPCMessage {
    private String name;
    
    public SetName() {
        
    }
    
    public SetName(String name) {
        this.name = name;
    }
    
    @Override
    public void read(PacketBuf reader) {
        name = reader.readString();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeString(name);
    }
    
    @Override
    public int id() {
        return RPCType.SetName.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onSetName(rpc, this);
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return String.format("SetName[ name=\"%s\" ]", name);
    }
}
