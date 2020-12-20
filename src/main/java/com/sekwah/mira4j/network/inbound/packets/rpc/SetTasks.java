package com.sekwah.mira4j.network.inbound.packets.rpc;

import java.util.Arrays;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;

public class SetTasks implements RPCMessage {
    private int playerId;
    private byte[] tasks;
    
    public SetTasks() {
        
    }
    
    public SetTasks(int playerId, byte[] tasks) {
        this.playerId = playerId;
        this.tasks = tasks;
    }
    
    public void read(PacketBuf reader) {
        playerId = reader.readUnsignedByte();
        int length = reader.readUnsignedPackedInt();
        tasks = reader.readBytes(length);
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedByte(playerId);
        writer.writeUnsignedPackedInt(tasks.length);
        writer.writeBytes(tasks);
    }
    
    public int id() {
        return RPCType.SetTasks.getId();
    }
    
    public int getPlayerId() {
        return playerId;
    }
    
    public byte[] getTasks() {
        return tasks;
    }
    
    @Override
    public String toString() {
        return String.format("SetTasks[ playerId=%d, tasks=%s ]", playerId, Arrays.toString(tasks));
    }
}
