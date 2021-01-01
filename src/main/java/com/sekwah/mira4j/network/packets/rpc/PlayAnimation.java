package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class PlayAnimation implements RPCMessage {
    private int taskId;
    
    public PlayAnimation() {
        
    }
    
    public PlayAnimation(int taskId) {
        this.taskId = taskId;
    }
    
    @Override
    public void read(PacketBuf reader) {
        taskId = reader.readUnsignedByte();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeUnsignedByte(taskId);
    }
    
    @Override
    public int id() {
        return RPCType.PlayAnimation.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onPlayAnimation(rpc, this);
    }
    
    public int getTaskId() {
        return taskId;
    }
    
    @Override
    public String toString() {
        return String.format("PlayAnimation[ taskId=%d ]", taskId);
    }
}
