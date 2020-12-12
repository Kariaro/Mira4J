package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets;

public class SetScanner implements RPCObject {
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
        return Packets.RPCType.SetScanner.getId();
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
