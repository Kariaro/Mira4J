package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class CompleteTask implements RPCMessage {
    private int taskIndex;
    
    public CompleteTask() {
        
    }
    
    public CompleteTask(int taskIndex) {
        this.taskIndex = taskIndex;
    }
    
    @Override
    public void read(PacketBuf reader) {
        taskIndex = reader.readUnsignedPackedInt();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeUnsignedPackedInt(taskIndex);
    }
    
    @Override
    public int id() {
        return RPCType.CompleteTask.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onCompleteTask(rpc, this);
    }
    
    public int getTaskIndex() {
        return taskIndex;
    }
    
    @Override
    public String toString() {
        return String.format("CompleteTask[ taskIndex=%d ]", taskIndex);
    }
}
