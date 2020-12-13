package com.sekwah.mira4j.network.inbound.packets.hazel;

import com.sekwah.mira4j.game.GameLobby;
import com.sekwah.mira4j.game.GameOptionsData;
import com.sekwah.mira4j.network.ClientListener;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;

public class HostGame implements HazelMessage {
    private GameOptionsData data;
    private int gameId;
    
    public HostGame() {
        
    }
    
    public HostGame(GameLobby lobby) {
        this(lobby.getGameId());
    }
    
    public HostGame(int gameId) {
        this.gameId = gameId;
    }
    
    @Override
    public void read(PacketBuf reader) {
        this.data = reader.readGameOptionsData();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeInt(gameId);
    }
    
    @Override
    public int id() {
        return HazelType.HostGame.getId();
    }

    @Override
    public void forwardPacket(ClientListener listener) {
        listener.onHostGame(this);
    }
    
    public GameOptionsData getGameOptionsData() {
        return data;
    }
}
