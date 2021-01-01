package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class SetSkin implements RPCMessage {
    private int skinId;
    
    public SetSkin() {
        
    }
    
    public SetSkin(int skinId) {
        this.skinId = skinId;
    }
    
    @Override
    public void read(PacketBuf reader) {
        skinId = reader.readUnsignedPackedInt();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeUnsignedPackedInt(skinId);
    }
    
    @Override
    public int id() {
        return RPCType.SetSkin.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onSetSkin(rpc, this);
    }
    
    public int getSkinId() {
        return skinId;
    }
    
    @Override
    public String toString() {
        return String.format("SetSkin[ skinId=%d ]", skinId);
    }
}
