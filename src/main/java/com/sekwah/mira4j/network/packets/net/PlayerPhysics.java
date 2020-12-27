package com.sekwah.mira4j.network.packets.net;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.NetType;

public class PlayerPhysics extends ComponentDB {
    public PlayerPhysics() {
        
    }
    
    @Override
    public void read(PacketBuf reader, boolean isSpawning) {
        netId = reader.readUnsignedPackedInt();
        reader.skipBytes(3); // Empty hazel message
    }
    
    @Override
    public void write(PacketBuf writer, boolean isSpawning) {
        writer.writeUnsignedPackedInt(netId);
        writer.writeShort(0);
        writer.writeByte(1);
    }

    @Override
    public int id() {
        return NetType.PlayerPhysics.getId();
    }
    
    @Override
    public String toString() {
        return String.format("PlayerPhysics[ netId=%d ]", netId);
    }
}
