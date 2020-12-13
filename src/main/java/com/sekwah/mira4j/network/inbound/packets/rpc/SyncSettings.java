package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.game.GameOptionsData;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets;

public class SyncSettings implements RPCMessage {
    private GameOptionsData data;
    
    public SyncSettings() {
        
    }
    
    public SyncSettings(GameOptionsData data) {
        this.data = data;
    }
    
    public void read(PacketBuf reader) {
        data = reader.readGameOptionsData();
    }
    
    public void write(PacketBuf writer) {
        writer.writeGameOptionsData(data);
    }
    
    public int id() {
        return Packets.RPCType.SyncSettings.getId();
    }
    
    public GameOptionsData getData() {
        return data;
    }
    
    @Override
    public String toString() {
        return String.format("SyncSettings[ data=%s ]", data);
    }
}
