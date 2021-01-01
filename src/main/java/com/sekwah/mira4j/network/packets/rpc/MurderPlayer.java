package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class MurderPlayer implements RPCMessage {
    private int victimNetId;
    
    public MurderPlayer() {
        
    }
    
    public MurderPlayer(int victimNetId) {
        this.victimNetId = victimNetId;
    }
    
    @Override
    public void read(PacketBuf reader) {
        victimNetId = reader.readUnsignedPackedInt();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeUnsignedPackedInt(victimNetId);
    }
    
    @Override
    public int id() {
        return RPCType.MurderPlayer.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onMurderPlayer(rpc, this);
    }
    
    public int getVictimNetId() {
        return victimNetId;
    }
    
    @Override
    public String toString() {
        return String.format("MurderPlayer[ victimNetId=%d ]", victimNetId);
    }
}
