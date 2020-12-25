package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;

public class PlayAnimation implements RPCMessage {
    private int taskId;
    
    public PlayAnimation() {
        
    }
    
    public PlayAnimation(int taskId) {
        this.taskId = taskId;
    }
    
    public void read(PacketBuf reader) {
        taskId = reader.readUnsignedByte();
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedByte(taskId);
    }
    
    public int id() {
        return RPCType.PlayAnimation.getId();
    }
    
    public int getTaskId() {
        return taskId;
    }
    
    @Override
    public String toString() {
        return String.format("PlayAnimation[ taskId=%d ]", taskId);
    }
}
