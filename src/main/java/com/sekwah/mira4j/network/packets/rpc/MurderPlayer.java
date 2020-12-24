package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;

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
        return RPCType.MurderPlayer.getId();
    }
    
    public int getVictimNetId() {
        return victimNetId;
    }
    
    @Override
    public String toString() {
        return String.format("MurderPlayer[ victimNetId=%d ]", victimNetId);
    }
}
