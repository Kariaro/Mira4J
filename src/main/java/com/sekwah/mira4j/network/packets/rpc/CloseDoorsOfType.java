package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class CloseDoorsOfType implements RPCMessage {
    private int systemId;
    
    public CloseDoorsOfType() {
        
    }
    
    public CloseDoorsOfType(int systemId) {
        this.systemId = systemId;
    }
    
    @Override
    public void read(PacketBuf reader) {
        systemId = reader.readUnsignedByte();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeUnsignedByte(systemId);
    }
    
    @Override
    public int id() {
        return RPCType.CloseDoorsOfType.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onCloseDoorsOfType(rpc, this);
    }
    
    public int getSystemId() {
        return systemId;
    }
    
    @Override
    public String toString() {
        return String.format("CloseDoorsOfType[ systemId=%d ]", systemId);
    }
}
