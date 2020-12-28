package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.config.DisconnectReason;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.decoder.ClientInListener;
import com.sekwah.mira4j.utils.GameUtils;

/**
 * Host-to-Game
 */
public class KickPlayer implements HazelMessage {
    private final Player sender;
    private int gameId;
    private int kickedClientId;
    private boolean isBanned;
    private DisconnectReason reason;
    
    protected KickPlayer(Player sender) {
        this.sender = sender;
    }
    
    // All outbound packets should be constructed with KickPlayer.of()
    private KickPlayer(int gameId, int kickedClientId, boolean isBanned, DisconnectReason reason) {
        this.sender = null;
        this.gameId = gameId;
        this.kickedClientId = kickedClientId;
        this.isBanned = isBanned;
        this.reason = reason;
    }
    
    @Override
    public void read(PacketBuf reader) {
        gameId = reader.readInt();
        kickedClientId = reader.readUnsignedPackedInt();
        isBanned = reader.readBoolean();
        if(reader.readableBytes() > 0) {
            reason = DisconnectReason.fromId(reader.readUnsignedByte());
        }
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeInt(gameId);
        writer.writeUnsignedPackedInt(kickedClientId);
        writer.writeBoolean(isBanned);
        writer.writeUnsignedByte(reason.getId());
    }
    
    @Override
    public int id() {
        return HazelType.KickPlayer.getId();
    }
    
    @Override
    public Player getSender() {
        return sender;
    }
    
    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onKickPlayer(this);
    }
    
    public int getGameId() {
        return gameId;
    }
    
    public int getKickedClientId() {
        return kickedClientId;
    }
    
    public boolean isBanned() {
        return isBanned;
    }
    
    public DisconnectReason getDisconnectReason() {
        return reason;
    }
    
    @Override
    public String toString() {
        return String.format("gameId='%s' clientId=%d isBanned=%s", GameUtils.getStringFromGameId(gameId), kickedClientId, isBanned);
    }
    
    public static KickPlayer of(Player kickedPlayer, boolean isBanned, DisconnectReason reason) {
        return new KickPlayer(kickedPlayer.getLobby().getGameId(), kickedPlayer.getClientId(), isBanned, reason);
    }
}
