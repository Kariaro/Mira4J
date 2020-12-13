package com.sekwah.mira4j.network.inbound.packets.rpc;

import java.util.Arrays;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets;

public class SetInfected implements RPCMessage {
    private byte[] impostors;
    
    public SetInfected() {
        
    }
    
    public SetInfected(byte... impostors) {
        this.impostors = impostors.clone();
    }
    
    public void read(PacketBuf reader) {
        int length = reader.readUnsignedPackedInt();
        impostors = reader.readBytes(length);
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedPackedInt(impostors.length);
        writer.writeBytes(impostors);
    }
    
    public int id() {
        return Packets.RPCType.SetInfected.getId();
    }
    
    public byte[] getImpostors() {
        return impostors;
    }
    
    @Override
    public String toString() {
        return String.format("SetInfected[ impostors=%s ]", Arrays.toString(impostors));
    }
}
