package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;

public class PlayAnimation implements RPCMessage {
    private byte taskId;
    
    public PlayAnimation() {
        
    }
    
    public PlayAnimation(byte taskId) {
        this.taskId = taskId;
    }
    
    public void read(PacketBuf reader) {
        taskId = reader.readByte();
    }
    
    public void write(PacketBuf writer) {
        writer.writeByte(taskId);
    }
    
    public int id() {
        return RPCType.PlayAnimation.getId();
    }
    
    public byte getTaskId() {
        return taskId;
    }
    
    @Override
    public String toString() {
        return String.format("PlayAnimation[ taskId=%d ]", taskId);
    }
}
