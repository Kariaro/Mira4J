package com.sekwah.mira4j.network.packets.net;

import java.util.ArrayList;
import java.util.List;

import com.sekwah.mira4j.config.PlayerInfo;
import com.sekwah.mira4j.config.TaskInfo;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.NetType;

public class GameData implements Component {
    private int netId;
    private List<PlayerInfo> list;
    
    public GameData() {
        
    }
    
    public GameData(int netId, PlayerInfo info) {
        this.netId = netId;
        list = new ArrayList<>();
        list.add(info);
    }
    
    @Override
    public void read(PacketBuf reader, boolean isSpawning) {
        netId = reader.readUnsignedPackedInt();
        list = new ArrayList<>();
        
        if (isSpawning) {
            reader = reader.readMessage();
        }
        
        int playerCount = isSpawning ? reader.readUnsignedPackedInt():reader.readUnsignedByte();
        for (int i = 0; i < playerCount; i++) {
            PlayerInfo player = new PlayerInfo();
            list.add(player);
            
            player.playerId = reader.readUnsignedByte();
            player.name = reader.readString();
            player.colorId = reader.readUnsignedPackedInt();
            player.hatId = reader.readUnsignedPackedInt();
            player.petId = reader.readUnsignedPackedInt();
            player.skinId = reader.readUnsignedPackedInt();
            player.flags = reader.readUnsignedByte();
            
            int length = reader.readUnsignedByte();
            player.tasks = new TaskInfo[length];
            
            for (int j = 0; j < length; j++) {
                TaskInfo task = new TaskInfo();
                task.taskId = reader.readUnsignedPackedInt();
                task.isCompleted = reader.readBoolean();
                player.tasks[j] = task;
            }
            
        }
        
        if (isSpawning) {
            reader.release();
        }
    }
    
    @Override
    public void write(PacketBuf writer, boolean isSpawning) {
        
    }

    @Override
    public int id() {
        return NetType.GameData.getId();
    }
    
    @Override
    public int getNetId() {
        return netId;
    }
    
    @Override
    public String toString() {
        return String.format("GameData[ netId=%s, list=%s ]", netId, list);
    }
    
}
