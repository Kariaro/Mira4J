package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.game.GameOptionsData;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class SyncSettings implements RPCMessage {
    private GameOptionsData data;
    
    public SyncSettings() {
        
    }
    
    public SyncSettings(GameOptionsData data) {
        this.data = data;
    }
    
    @Override
    public void read(PacketBuf reader) {
        data = reader.readGameOptionsData();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeGameOptionsData(data);
    }
    
    @Override
    public int id() {
        return RPCType.SyncSettings.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onSyncSettings(rpc, this);
    }
    
    public GameOptionsData getData() {
        return data;
    }
    
    @Override
    public String toString() {
        return String.format("SyncSettings[ data=%s ]", data);
    }
}
