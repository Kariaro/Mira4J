package com.sekwah.mira4j.network.packets.rpc;

import java.util.Arrays;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class SetInfected implements RPCMessage {
    private byte[] impostors;
    
    public SetInfected() {
        
    }
    
    public SetInfected(byte... impostors) {
        this.impostors = impostors.clone();
    }
    
    @Override
    public void read(PacketBuf reader) {
        int length = reader.readUnsignedPackedInt();
        impostors = reader.readBytes(length);
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeUnsignedPackedInt(impostors.length);
        writer.writeBytes(impostors);
    }
    
    @Override
    public int id() {
        return RPCType.SetInfected.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onSetInfected(rpc, this);
    }
    
    public byte[] getImpostors() {
        return impostors;
    }
    
    @Override
    public String toString() {
        return String.format("SetInfected[ impostors=%s ]", Arrays.toString(impostors));
    }
}
