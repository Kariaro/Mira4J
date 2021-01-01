package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

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
    
    @Override
    public void read(PacketBuf reader) {
        systemId = reader.readUnsignedByte();
        playerControlNetId = reader.readUnsignedPackedInt();
        amount = reader.readUnsignedByte();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeUnsignedByte(systemId);
        writer.writeUnsignedPackedInt(playerControlNetId);
        writer.writeUnsignedByte(amount);
    }
    
    @Override
    public int id() {
        return RPCType.RepairSystem.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onRepairSystem(rpc, this);
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
