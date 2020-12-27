package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.api.GameLobby;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.decoder.ClientInListener;

public class StartGame implements HazelMessage {
    private int gameId;
    
    public StartGame() {
        
    }
    
    public StartGame(GameLobby lobby) {
        this(lobby.getGameId());
    }
    
    public StartGame(int gameId) {
        this.gameId = gameId;
    }
    
    @Override
    public void read(PacketBuf reader) {
        gameId = reader.readInt();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeInt(gameId);
    }
    
    @Override
    public int id() {
        return HazelType.StartGame.getId();
    }
    
    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onStartGame(this);
    }
    
    public int getGameId() {
        return gameId;
    }
}
