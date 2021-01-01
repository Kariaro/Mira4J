package com.sekwah.mira4j.network.packets.rpc;

import java.util.ArrayList;
import java.util.List;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.config.PlayerInfo;
import com.sekwah.mira4j.config.TaskInfo;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class UpdateGameData implements RPCMessage {
    private List<PlayerInfo> list;
    
    public UpdateGameData() {
        
    }
    
    public UpdateGameData(Scene scene) {
        list = new ArrayList<>();
        for(Player player : scene.getPlayers()) {
            if(player.isDirty()) {
                list.add(new PlayerInfo(player));
            }
        }
    }
    
    @Override
    public void read(PacketBuf reader) {
        list = new ArrayList<>();
        
        while (reader.readableBytes() > 0) {
            PacketBuf buf = reader.readMessageKeepId();
            try {
                PlayerInfo info = new PlayerInfo();
                list.add(info);
                info.playerId = buf.readUnsignedByte();
                
                
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
            } finally {
                buf.release();
            }
        }
    }
    
    @Override
    public void write(PacketBuf writer) {
        for (PlayerInfo info : list) {
            writer.startMessage(info.playerId);
            writer.writeString(info.name);
            writer.writeUnsignedByte(info.colorId);
            writer.writeUnsignedPackedInt(info.hatId);
            writer.writeUnsignedPackedInt(info.petId);
            writer.writeUnsignedPackedInt(info.skinId);
            writer.writeUnsignedByte(info.flags);
            
            TaskInfo[] tasks = info.tasks;
            writer.writeUnsignedByte(tasks.length);
            for (TaskInfo task : tasks) {
                writer.writeUnsignedPackedInt(task.taskId);
                writer.writeBoolean(task.isCompleted);
            }
            
            writer.endMessage();
        }
    }
    
    @Override
    public int id() {
        return RPCType.UpdateGameData.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onUpdateGameData(rpc, this);
    }
    
    public List<PlayerInfo> getPlayers() {
        return list;
    }
    
    @Override
    public String toString() {
        return String.format("UpdateGameData[ list=%s ]", list);
    }
}
