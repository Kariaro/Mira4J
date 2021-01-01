package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class CheckColor implements RPCMessage {
    private int colorId;
    
    public CheckColor() {
        
    }
    
    public CheckColor(int colorId) {
        this.colorId = colorId;
    }
    
    @Override
    public void read(PacketBuf reader) {
        colorId = reader.readUnsignedByte();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeUnsignedByte(colorId);
    }
    
    @Override
    public int id() {
        return RPCType.CheckColor.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onCheckColor(rpc, this);
    }
    
    public int getColorId() {
        return colorId;
    }
    
    @Override
    public String toString() {
        return String.format("CheckColor[ colorId=%d ]", colorId);
    }
}
