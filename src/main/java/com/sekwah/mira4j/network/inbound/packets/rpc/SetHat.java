package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets;

public class SetHat implements RPCObject {
    private int hatId;
    
    public SetHat() {
        
    }
    
    public SetHat(int hatId) {
        this.hatId = hatId;
    }
    
    public void read(PacketBuf reader) {
        hatId = reader.readUnsignedPackedInt();
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedPackedInt(hatId);
    }
    
    public int id() {
        return Packets.RPCType.SetHat.getId();
    }
    
    public int getHatId() {
        return hatId;
    }
    
    @Override
    public String toString() {
        return String.format("SetHat[ hatId=%d ]", hatId);
    }
}
