package com.sekwah.mira4j.network.inbound.packets.net;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.NetType;

public class LobbyBehaviour implements Component {
    private int netId;
    
    @Override
    public void read(PacketBuf reader, boolean isSpawning) {
        netId = reader.readUnsignedPackedInt();
        // Empty packet
    }
    
    @Override
    public void write(PacketBuf writer, boolean isSpawning) {
        
    }
    
    @Override
    public int getNetId() {
        return netId;
    }
    
    @Override
    public int id() {
        return NetType.LobbyBehaviour.getId();
    }
    
    @Override
    public String toString() {
        return String.format("LobbyBehaviour[ netId=%d ]", netId);
    }
}
