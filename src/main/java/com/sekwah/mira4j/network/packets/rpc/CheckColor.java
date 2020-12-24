package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;

public class CheckColor implements RPCMessage {
    private int colorId;
    
    public CheckColor() {
        
    }
    
    public CheckColor(int colorId) {
        this.colorId = colorId;
    }
    
    public void read(PacketBuf reader) {
        colorId = reader.readUnsignedByte();
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedByte(colorId);
    }
    
    public int id() {
        return RPCType.CheckColor.getId();
    }
    
    public int getColorId() {
        return colorId;
    }
    
    @Override
    public String toString() {
        return String.format("CheckColor[ colorId=\"%d\" ]", colorId);
    }
}
