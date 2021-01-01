package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class EnterVent implements RPCMessage {
    private int ventId;
    
    public EnterVent() {
        
    }
    
    public EnterVent(int ventId) {
        this.ventId = ventId;
    }
    
    @Override
    public void read(PacketBuf reader) {
        ventId = reader.readUnsignedPackedInt();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeUnsignedPackedInt(ventId);
    }
    
    @Override
    public int id() {
        return RPCType.EnterVent.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onEnterVent(rpc, this);
    }
    
    public int getVentId() {
        return ventId;
    }
    
    @Override
    public String toString() {
        return String.format("EnterVent[ ventId=%d ]", ventId);
    }
}
