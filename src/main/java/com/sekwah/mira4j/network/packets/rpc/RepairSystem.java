package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;

public class RepairSystem implements RPCMessage {
    private int systemId;
    private int playerControlNetId;
    private int amount;
    
    public RepairSystem() {
        
    }
    
    public RepairSystem(int systemId, int playerControlNetId, int amount) {
        this.systemId = systemId;
        this.playerControlNetId = playerControlNetId;
        this.amount = amount;
    }
    
    public void read(PacketBuf reader) {
        systemId = reader.readUnsignedByte();
        playerControlNetId = reader.readUnsignedPackedInt();
        amount = reader.readUnsignedByte();
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedByte(systemId);
        writer.writeUnsignedPackedInt(playerControlNetId);
        writer.writeUnsignedByte(amount);
    }
    
    public int id() {
        return RPCType.RepairSystem.getId();
    }
    
    public int getSystemId() {
        return systemId;
    }
    
    public int getPlayerControlNetId() {
        return playerControlNetId;
    }
    
    public int getAmount() {
        return amount;
    }
    
    @Override
    public String toString() {
        return String.format("RepairSystem[ systemId=%d, playerControlNetId=%d, amount=%d ]", systemId, playerControlNetId, amount);
    }
}
