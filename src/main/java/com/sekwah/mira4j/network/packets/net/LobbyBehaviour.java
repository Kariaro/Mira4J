package com.sekwah.mira4j.network.packets.net;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.NetType;
import com.sekwah.mira4j.network.decoder.NetListener;

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
    public void forwardPacket(NetListener listener) {
        listener.onLobbyBehaviour(this);
    }
    
    @Override
    public String toString() {
        return String.format("LobbyBehaviour[ netId=%d ]", netId);
    }
}
