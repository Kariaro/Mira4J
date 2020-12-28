package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.decoder.ClientInListener;

/**
 * Host-to-Game
 */
public class AlterGame implements HazelMessage {
    private final Player sender;
    private int gameId;
    private int tagId;
    private int tagValue;
    
    protected AlterGame(Player sender) {
        this.sender = sender;
    }
    
    private AlterGame(Player sender, int gameId, int tagId, int tagValue) {
        this.sender = sender;
        this.gameId = gameId;
        this.tagId = tagId;
        this.tagValue = tagValue;
    }
    
    @Override
    public void read(PacketBuf reader) {
        gameId = reader.readInt();
        tagId = reader.readUnsignedByte();
        tagValue = reader.readUnsignedByte();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeInt(gameId);
        writer.writeUnsignedByte(tagId);
        writer.writeUnsignedByte(tagValue);
    }
    
    @Override
    public int id() {
        return HazelType.AlterGame.getId();
    }
    
    @Override
    public Player getSender() {
        return sender;
    }
    
    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onAlterGame(this);
    }
    
    @Override
    public int getGameId() {
        return gameId;
    }
    
    public int getTagId() {
        return tagId;
    }
    
    public int getTagValue() {
        return tagValue;
    }
    
    public static AlterGame of(Player sender, int tagId, int tagValue) {
        return new AlterGame(sender, sender.getLobby().getGameId(), tagId, tagValue);
    }
}
