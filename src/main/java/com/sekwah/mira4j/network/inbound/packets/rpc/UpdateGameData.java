package com.sekwah.mira4j.network.inbound.packets.rpc;

import java.util.ArrayList;
import java.util.List;

import com.sekwah.mira4j.config.PlayerInfo;
import com.sekwah.mira4j.config.TaskInfo;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;

public class UpdateGameData implements RPCMessage {
    private List<PlayerInfo> list;
    
    public UpdateGameData() {
        
    }
    
    public UpdateGameData(List<PlayerInfo> list) {
        this.list = new ArrayList<>();
        this.list.addAll(list);
    }
    
    public UpdateGameData(PlayerInfo... array) {
        list = new ArrayList<>();
        for (PlayerInfo info : array) {
            list.add(info);
        }
    }
    
    public void read(PacketBuf reader) {
        list = new ArrayList<>();
        while (reader.readableBytes() > 0) {
            int length = reader.readUnsignedShort();
            int playerId = reader.readUnsignedByte();
            byte[] bytes = reader.readBytes(length);
            
            PlayerInfo info = new PlayerInfo();
            info.playerId = playerId;
            
            PacketBuf buf = PacketBuf.wrap(bytes);
            info.name = buf.readString();
            info.colorId = buf.readUnsignedByte();
            info.hatId = buf.readUnsignedPackedInt();
            info.petId = buf.readUnsignedPackedInt();
            info.skinId = buf.readUnsignedPackedInt();
            info.flags = buf.readUnsignedByte();
            
            int tasks_length = buf.readUnsignedByte();
            info.tasks = new TaskInfo[tasks_length];
            
            for (int i = 0; i < tasks_length; i++) {
                TaskInfo task = new TaskInfo();
                task.taskId = buf.readUnsignedPackedInt();
                task.isCompleted = buf.readBoolean();
                info.tasks[i] = task;
            }
            
            buf.release();
        }
    }
    
    public void write(PacketBuf writer) {
        for (PlayerInfo info : list) {
            PacketBuf buf = PacketBuf.create(4096);
            buf.writeString(info.name);
            buf.writeUnsignedByte(info.colorId);
            buf.writeUnsignedPackedInt(info.hatId);
            buf.writeUnsignedPackedInt(info.petId);
            buf.writeUnsignedPackedInt(info.skinId);
            buf.writeUnsignedByte(info.flags);
            buf.writeUnsignedByte(info.tasks.length);
            for (TaskInfo task : info.tasks) {
                buf.writeUnsignedPackedInt(task.taskId);
                buf.writeBoolean(task.isCompleted);
            }
            
            byte[] bytes = buf.readBytes(buf.readableBytes());
            buf.release();
            
            writer.writeShort(bytes.length);
            writer.writeByte(info.playerId);
            writer.writeBytes(bytes);
        }
    }
    
    public int id() {
        return RPCType.UpdateGameData.getId();
    }
    
    public List<PlayerInfo> getPlayer() {
        return list;
    }
    
    @Override
    public String toString() {
        return String.format("UpdateGameData[ list=%s ]", list);
    }
}
