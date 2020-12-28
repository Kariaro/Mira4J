package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.api.GameLobby;
import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.config.DisconnectReason;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.decoder.ClientInListener;

/**
 * Host-to-Client<br>
 * Server-to-Game
 */
public class RemovePlayer implements HazelMessage {
    private final Player sender;
    private int gameId;
    private int disconnectedClientId; 
    private int hostClientId;
    private DisconnectReason reason;
    
    protected RemovePlayer(Player sender) {
        this.sender = sender;
    }
    
    private RemovePlayer(GameLobby lobby, Player player, DisconnectReason reason) {
        this.sender = null;
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
    public Player getSender() {
        return sender;
    }
    
    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onRemovePlayer(this);
    }
    
    @Override
    public int id() {
        return HazelType.RemovePlayer.getId();
    }
    
    @Override
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
