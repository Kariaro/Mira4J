package com.sekwah.mira4j.network.packets.net;

import java.util.ArrayList;
import java.util.List;

import com.sekwah.mira4j.Mira4J;
import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.config.TaskInfo;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.NetType;
import com.sekwah.mira4j.network.decoder.NetListener;

public class NetGameData extends ComponentDB {
    private List<Player> list;
    
    public NetGameData() {
        list = new ArrayList<>();
    }
    
    @Override
    public void read(PacketBuf reader, boolean isSpawning) {
        netId = reader.readUnsignedPackedInt();
        list = new ArrayList<>();
        
        if (isSpawning) {
            reader = reader.readMessage();
            if(scene == null) {
                Mira4J.LOGGER.warn("Scene was null ! {}", this);
                return; // bad
            }
        }
        
        int playerCount = isSpawning ? reader.readUnsignedPackedInt():reader.readUnsignedByte();
        for (int i = 0; i < playerCount; i++) {
//            PlayerInfo player = new PlayerInfo();
//            list.add(player);
//            
//            player.playerId = reader.readUnsignedByte();
//            
            Player player = scene.getPlayer(reader.readUnsignedByte());
            String name = reader.readString();
            int colorId = reader.readUnsignedPackedInt();
            int hatId = reader.readUnsignedPackedInt();
            int petId = reader.readUnsignedPackedInt();
            int skinId = reader.readUnsignedPackedInt();
            int flags = reader.readUnsignedByte();
            
            int length = reader.readUnsignedByte();
            TaskInfo[] tasks = new TaskInfo[length];
            
            for (int j = 0; j < length; j++) {
                TaskInfo task = new TaskInfo();
                task.taskId = reader.readUnsignedPackedInt();
                task.isCompleted = reader.readBoolean();
                tasks[j] = task;
            }
            
            if(player != null) {
                player.setName(name);
                player.setColorId(colorId);
                player.setHatId(hatId);
                player.setPetId(petId);
                player.setSkinId(skinId);
                player.setFlags(flags);
                // TODO: Tasks!!!
            }
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
        
        int writerIndex = writer.writerIndex();
        if (isSpawning) {
            writer.writeUnsignedPackedInt(list.size());
        } else {
            writer.writeByte(0);
        }
        
        int dirtyCount = 0;
        for (int i = 0; i < 16; i++) {
            Player player = scene.getPlayer(i);
            if(player == null || !player.isDirty()) continue;
            dirtyCount++;
            
            writer.writeByte(i);
            writer.writeString(player.getName());
            writer.writeUnsignedPackedInt(player.getColorId());
            writer.writeUnsignedPackedInt(player.getHatId());
            writer.writeUnsignedPackedInt(player.getPetId());
            writer.writeUnsignedPackedInt(player.getSkinId());
            writer.writeByte(player.getFlags());
            writer.writeByte(0); // Tasks are disabled for now
        }
        
        if (!isSpawning) {
            writer.markWriterIndex();
            writer.writerIndex(writerIndex);
            writer.writeUnsignedByte(dirtyCount);
            writer.resetWriterIndex();
        } else {
            writer.endMessage();
        }
    }

    @Override
    public int id() {
        return NetType.NetGameData.getId();
    }
    
    @Override
    public void forwardPacket(NetListener listener) {
        listener.onNetGameData(this);
    }
    
    @Override
    public String toString() {
        return String.format("GameData[ netId=%d, list=%s ] %d", netId, list, hashCode());
    }
    
}
