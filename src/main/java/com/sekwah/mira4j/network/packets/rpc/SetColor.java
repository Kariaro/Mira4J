package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;

public class SetColor implements RPCMessage {
    private int colorId;
    
    public SetColor() {
        
    }
    
    public SetColor(int colorId) {
        this.colorId = colorId;
    }
    
    public void read(PacketBuf reader) {
        colorId = reader.readUnsignedByte();
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedByte(colorId);
    }
    
    public int id() {
        return RPCType.SetColor.getId();
    }
    
    public int getColorId() {
        return colorId;
    }
    
    @Override
    public String toString() {
        return String.format("SetColor[ colorId=%d ]", colorId);
    }
}