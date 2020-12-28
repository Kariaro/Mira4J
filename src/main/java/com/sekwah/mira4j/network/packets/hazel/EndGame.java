package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.config.GameOverReason;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.decoder.ClientInListener;

/**
 * Host-to-Game
 */
public class EndGame implements HazelMessage {
    private final Player sender;
    private int gameId;
    private GameOverReason reason;
    private boolean showAd;
    
    protected EndGame(Player sender) {
        this.sender = sender;
    }
    
    public EndGame(Player sender, int gameId, GameOverReason reason, boolean showAd) {
        this.sender = sender;
        this.gameId = gameId;
        this.reason = reason;
        this.showAd = showAd;
    }
    
//    public EndGame(GameLobby lobby, GameOverReason reason, boolean showAd) {
//        this(lobby.getGameId(), reason, showAd);
//    }
    
    @Override
    public void read(PacketBuf reader) {
        gameId = reader.readInt();
        reason = GameOverReason.fromId(reader.readUnsignedByte());
        showAd = reader.readBoolean();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeInt(gameId);
        writer.writeUnsignedByte(reason.getId());
        writer.writeBoolean(showAd);
    }
    
    @Override
    public int id() {
        return HazelType.EndGame.getId();
    }
    
    @Override
    public Player getSender() {
        return sender;
    }
    
    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onEndGame(this);
    }
    
    public int getGameId() {
        return gameId;
    }
    
    public GameOverReason getGameOverReason() {
        return reason;
    }
    
    public boolean getShowAd() {
        return showAd;
    }
}
