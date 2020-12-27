package com.sekwah.mira4j.network.packets.net;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.NetType;

public class LobbyBehaviour extends ComponentDB {
    public LobbyBehaviour() {
        
    }
    
    @Override
    public void read(PacketBuf reader, boolean isSpawning) {
        netId = reader.readUnsignedPackedInt();
        // Empty packet
    }
    
    @Override
    public void write(PacketBuf writer, boolean isSpawning) {
        
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
