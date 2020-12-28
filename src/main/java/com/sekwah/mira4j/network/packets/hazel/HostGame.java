package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.api.GameLobby;
import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.game.GameOptionsData;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.decoder.ClientInListener;
import com.sekwah.mira4j.utils.NonNull;

/**
 * Client-to-Server<br>
 * Server-to-Client
 */
public class HostGame implements HazelMessage {
    private final Player sender;
    private GameOptionsData data;
    private int gameId;
    
    protected HostGame(Player sender) {
        this.sender = sender;
    }
    
    private HostGame(int gameId) {
        this.sender = null;
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
    public Player getSender() {
        return sender;
    }
    
    @Override
    public int getGameId() {
        return gameId;
    }

    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onHostGame(this);
    }
    
    public GameOptionsData getGameOptionsData() {
        return data;
    }
    
    @Override
    public String toString() {
        return String.format("data=%s", data);
    }
    
    public static HostGame of(@NonNull GameLobby lobby) {
        return new HostGame(lobby.getGameId());
    }
}
