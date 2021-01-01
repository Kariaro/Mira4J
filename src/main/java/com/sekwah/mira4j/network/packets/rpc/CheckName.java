package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class CheckName implements RPCMessage {
    private String name;
    
    public CheckName() {
        
    }
    
    public CheckName(String name) {
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
        return RPCType.CheckName.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onCheckName(rpc, this);
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return String.format("CheckName[ name=\"%s\" ]", name);
    }
}
