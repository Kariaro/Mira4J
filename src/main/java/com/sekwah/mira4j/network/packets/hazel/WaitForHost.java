package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.decoder.ClientInListener;

/**
 * Client-to-Host
 */
public class WaitForHost implements HazelMessage {
    private final Player sender;
    private int gameId;
    private int rejoiningClientId;
    
    protected WaitForHost(Player sender) {
        this.sender = sender;
    }
    
    public WaitForHost(Player sender, int gameId, int rejoiningClientId) {
        this.sender = sender;
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
    public Player getSender() {
        return sender;
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
