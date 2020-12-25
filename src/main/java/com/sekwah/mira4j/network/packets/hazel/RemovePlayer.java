package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.config.DisconnectReason;
import com.sekwah.mira4j.game.GameLobby;
import com.sekwah.mira4j.impl.unity.PlayerDB;
import com.sekwah.mira4j.network.ClientListener;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;

public class RemovePlayer implements HazelMessage {
    private int gameId;
    private int disconnectedClientId; 
    private int hostClientId;
    private DisconnectReason reason;
    
    public RemovePlayer() {
        
    }
    
    public RemovePlayer(GameLobby lobby, PlayerDB player, DisconnectReason reason) {
        this.reason = reason;
        this.gameId = lobby.getGameId();
        this.hostClientId = lobby.getHost().getClientId();
    }
    
    @Override
    public void read(PacketBuf reader) {
        gameId = reader.readInt();
        disconnectedClientId = reader.readUnsignedPackedInt();
        reason = DisconnectReason.fromId(reader.readUnsignedByte());
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeInt(gameId);
        writer.writeUnsignedInt(disconnectedClientId);
        writer.writeUnsignedInt(hostClientId);
        writer.writeByte(reason.getId());
    }
    
    @Override
    public void forwardPacket(ClientListener listener) {
        listener.onRemovePlayer(this);
    }
    
    @Override
    public int id() {
        return HazelType.RemovePlayer.getId();
    }
    
    public int getGameId() {
        return gameId;
    }
    
    public int getDisconnectedClientId() {
        return disconnectedClientId;
    }
    
    public DisconnectReason getDisconnectReason() {
        return reason;
    }
}
