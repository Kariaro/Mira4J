package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.api.GameLobby;
import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.config.DisconnectReason;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.decoder.ClientInListener;
import com.sekwah.mira4j.utils.GameUtils;

/**
 * Client-to-Server<br>
 * Server-to-Game<br>
 * Server-to-Client
 */
public class JoinGame implements HazelMessage {
    private final Player sender;
    private int gameId;
    private int ownedMaps;
    private int clientId;
    private int hostId;
    private DisconnectReason reason;
    
    protected JoinGame(Player sender) {
        this.sender = sender;
    }
    
    private JoinGame(Player sender, int gameId, int clientId, int hostId) {
        this.sender = sender;
        this.gameId = gameId;
        this.clientId = clientId;
        this.hostId = hostId;
    }
    
    private JoinGame(Player sender, DisconnectReason reason) {
        this.sender = sender;
        this.reason = reason;
    }

    @Override
    public void read(PacketBuf reader) {
        gameId = reader.readInt();
        ownedMaps = reader.readUnsignedByte();
    }
    
    @Override
    public void write(PacketBuf writer) {
        if(reason != null) {
            writer.writeByte(reason.getId());
        } else {
            writer.writeInt(gameId);
            writer.writeUnsignedInt(clientId);
            writer.writeUnsignedInt(hostId);   
        }
    }
    
    @Override
    public int id() {
        return HazelType.JoinGame.getId();
    }
    
    @Override
    public Player getSender() {
        return sender;
    }
    
    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onJoinGame(this);
    }
    
    public int getGameId() {
        return gameId;
    }
    
    public int getOwnedMaps() {
        return ownedMaps;
    }
    
    @Override
    public String toString() {
        return String.format("gameId='%s' ownedMaps=%d", GameUtils.getStringFromGameId(gameId), ownedMaps);
    }
    
    public static JoinGame of(Player player, GameLobby lobby) {
        return new JoinGame(
            player,
            lobby.getGameId(),
            player.getClientId(),
            lobby.getHost().getClientId()
        );
    }
    
    public static JoinGame of(Player player, DisconnectReason reason) {
        return new JoinGame(player, reason);
    }
}
