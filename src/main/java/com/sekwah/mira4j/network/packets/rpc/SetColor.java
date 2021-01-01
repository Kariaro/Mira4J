package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class SetColor implements RPCMessage {
    private int colorId;
    
    public SetColor() {
        
    }
    
    public SetColor(int colorId) {
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
        return RPCType.SetColor.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onSetColor(rpc, this);
    }
    
    public int getColorId() {
        return colorId;
    }
    
    @Override
    public String toString() {
        return String.format("SetColor[ colorId=%d ]", colorId);
    }
}
