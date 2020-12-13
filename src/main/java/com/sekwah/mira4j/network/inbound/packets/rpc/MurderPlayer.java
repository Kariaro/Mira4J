package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets;

public class MurderPlayer implements RPCMessage {
    private int victimNetId;
    
    public MurderPlayer() {
        
    }
    
    public MurderPlayer(int victimNetId) {
        this.victimNetId = victimNetId;
    }
    
    public void read(PacketBuf reader) {
        victimNetId = reader.readUnsignedPackedInt();
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedPackedInt(victimNetId);
    }
    
    public int id() {
        return Packets.RPCType.MurderPlayer.getId();
    }
    
    public int getVictimNetId() {
        return victimNetId;
    }
    
    @Override
    public String toString() {
        return String.format("MurderPlayer[ victimNetId=%d ]", victimNetId);
    }
}
