package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.config.DisconnectReason;
import com.sekwah.mira4j.network.ClientListener;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;

public class KickPlayer implements HazelMessage {
    private int gameId;
    private int kickedClientId;
    private boolean isBanned;
    private DisconnectReason reason;
    
    public KickPlayer() {
        
    }
    
    public KickPlayer(int gameId, int kickedClientId, boolean isBanned, DisconnectReason reason) {
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
    public void forwardPacket(ClientListener listener) {
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
    
}
