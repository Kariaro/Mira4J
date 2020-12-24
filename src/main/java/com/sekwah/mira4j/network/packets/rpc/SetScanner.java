package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;

public class SetScanner implements RPCMessage {
    private boolean isScanning;
    private int count;
    
    public SetScanner() {
        
    }
    
    public SetScanner(boolean isScanning, int count) {
        this.isScanning = isScanning;
        this.count = count;
    }
    
    public void read(PacketBuf reader) {
        isScanning = reader.readBoolean();
        count = reader.readUnsignedByte();
    }
    
    public void write(PacketBuf writer) {
        writer.writeBoolean(isScanning);
        writer.writeUnsignedByte(count);
    }
    
    public int id() {
        return RPCType.SetScanner.getId();
    }
    
    public boolean isScanning() {
        return isScanning;
    }
    
    public int getCount() {
        return count;
    }
    
    @Override
    public String toString() {
        return String.format("SetScanner[ isScanning=%s, count=%d ]", isScanning, count);
    }
}