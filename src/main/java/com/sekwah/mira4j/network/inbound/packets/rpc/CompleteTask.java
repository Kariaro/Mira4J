package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;

public class CompleteTask implements RPCMessage {
    private int taskIndex;
    
    public CompleteTask() {
        
    }
    
    public CompleteTask(int taskIndex) {
        this.taskIndex = taskIndex;
    }
    
    public void read(PacketBuf reader) {
        taskIndex = reader.readUnsignedPackedInt();
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedPackedInt(taskIndex);
    }
    
    public int id() {
        return RPCType.CompleteTask.getId();
    }
    
    public int getTaskIndex() {
        return taskIndex;
    }
    
    @Override
    public String toString() {
        return String.format("CompleteTask[ taskIndex=%d ]", taskIndex);
    }
}
