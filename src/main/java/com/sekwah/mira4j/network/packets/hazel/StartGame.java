package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.api.GameLobby;
import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.decoder.ClientInListener;
import com.sekwah.mira4j.utils.NonNull;

/**
 * Host-to-Game
 */
public class StartGame implements HazelMessage {
    private final Player sender;
    private int gameId;
    
    protected StartGame(Player sender) {
        this.sender = sender;
    }
    
    private StartGame(int gameId) {
        this.sender = null;
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
    public Player getSender() {
        return sender;
    }
    
    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onStartGame(this);
    }
    
    public int getGameId() {
        return gameId;
    }
    
    public static StartGame of(@NonNull GameLobby lobby) {
        return new StartGame(lobby.getGameId());
    }
}
