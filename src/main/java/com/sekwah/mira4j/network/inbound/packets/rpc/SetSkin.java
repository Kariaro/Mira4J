package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets;

public class SetSkin implements RPCMessage {
    private int skinId;
    
    public SetSkin() {
        
    }
    
    public SetSkin(int skinId) {
        this.skinId = skinId;
    }
    
    public void read(PacketBuf reader) {
        skinId = reader.readUnsignedPackedInt();
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedPackedInt(skinId);
    }
    
    public int id() {
        return Packets.RPCType.SetSkin.getId();
    }
    
    public int getSkinId() {
        return skinId;
    }
    
    @Override
    public String toString() {
        return String.format("SetSkin[ skinId=%d ]", skinId);
    }
}
