package com.sekwah.mira4j.network.inbound.packets.net;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.NetType;

public class PlayerControl implements Component {
    private int netId;
    private int playerId;
    private boolean isNew;
    
    @Override
    public void read(PacketBuf reader, boolean isSpawning) {
        netId = reader.readUnsignedPackedInt();
        
        if(isSpawning) {
            reader = reader.readMessage();
            // isNew = reader.readBoolean();
            
            if(reader.readableBytes() == 0) {
                reader.release();
                return;
            }
        }
        
        playerId = reader.readUnsignedByte();
        
        if(isSpawning) {
            reader.release();
        }
    }
    
    @Override
    public void write(PacketBuf writer, boolean isSpawning) {
        
    }

    @Override
    public int id() {
        return NetType.PlayerControl.getId();
    }
    
    public int getNetId() {
        return netId;
    }
    
    public boolean isNew() {
        return isNew;
    }
    
    public int getPlayerId() {
        return playerId;
    }
    
    @Override
    public String toString() {
        return String.format("PlayerControl[ netId=%d, isNew=%s, playerId=%d ]", netId, isNew, playerId);
    }
}
