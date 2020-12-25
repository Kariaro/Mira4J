package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.decoder.ClientInListener;

public class WaitForHost implements HazelMessage {
    private int gameId;
    private int rejoiningClientId;
    
    public WaitForHost() {
        
    }
    
    public WaitForHost(int gameId, int rejoiningClientId) {
        this.gameId = gameId;
        this.rejoiningClientId = rejoiningClientId;
    }
    
    @Override
    public void read(PacketBuf reader) {
        gameId = reader.readInt();
        rejoiningClientId = reader.readInt();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeInt(gameId);
        writer.writeInt(rejoiningClientId);
    }
    
    @Override
    public int id() {
        return HazelType.WaitForHost.getId();
    }
    
    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onWaitForHost(this);
    }
    
    public int getGameId() {
        return gameId;
    }
    
    public int getRejoiningClientId() {
        return rejoiningClientId;
    }
}
