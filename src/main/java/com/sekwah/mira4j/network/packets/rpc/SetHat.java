package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class SetHat implements RPCMessage {
    private int hatId;
    
    public SetHat() {
        
    }
    
    public SetHat(int hatId) {
        this.hatId = hatId;
    }
    
    @Override
    public void read(PacketBuf reader) {
        hatId = reader.readUnsignedPackedInt();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeUnsignedPackedInt(hatId);
    }
    
    @Override
    public int id() {
        return RPCType.SetHat.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onSetHat(rpc, this);
    }
    
    public int getHatId() {
        return hatId;
    }
    
    @Override
    public String toString() {
        return String.format("SetHat[ hatId=%d ]", hatId);
    }
}
