package com.sekwah.mira4j.network.inbound.packets.net;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.NetType;

public class PlayerPhysics implements Component {
    private int netId;
    
    @Override
    public void read(PacketBuf reader, boolean isSpawning) {
        netId = reader.readUnsignedPackedInt();
    }
    
    @Override
    public void write(PacketBuf writer, boolean isSpawning) {
        
    }

    @Override
    public int id() {
        return NetType.PlayerPhysics.getId();
    }
    
    public int getNetId() {
        return netId;
    }
    
    @Override
    public String toString() {
        return String.format("PlayerPhysics[ netId=%d ]", netId);
    }
}
