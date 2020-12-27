package com.sekwah.mira4j.network.packets.net;

import java.util.ArrayList;
import java.util.List;

import com.sekwah.mira4j.config.PlayerInfo;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.NetType;

public class VoteBanSystem extends ComponentDB {
    private List<PlayerInfo> list;
    
    public VoteBanSystem() {
        list = new ArrayList<>();
    }
    
    @Override
    public void read(PacketBuf reader, boolean isSpawning) {
        netId = reader.readUnsignedPackedInt();
        list = new ArrayList<>();
        
        if (isSpawning) {
            reader = reader.readMessage();
        }
        
        int playerCount = reader.readUnsignedByte();
        for (int i = 0; i < playerCount; i++) {
            PlayerInfo player = new PlayerInfo();
            list.add(player);
            
            int clientId = reader.readInt();
            
            // TODO: Bad documentation?
        }
        
        if (isSpawning) {
            reader.release();
        }
    }
    
    @Override
    public void write(PacketBuf writer, boolean isSpawning) {
        writer.writeUnsignedPackedInt(netId);
        
        if (isSpawning) {
            writer.startMessage(0);
        }
        
        writer.writeByte(list.size());
        
        if (isSpawning) {
            writer.endMessage();
        }
    }

    @Override
    public int id() {
        return NetType.NetGameData.getId();
    }
    
    @Override
    public String toString() {
        return String.format("VoteBanSystem[ netId=%s, list=%s ]", netId, list);
    }
    
}
