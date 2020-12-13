package com.sekwah.mira4j.network.inbound.packets.hazel;

import com.sekwah.mira4j.network.ClientListener;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;

public class AlterGame implements HazelMessage {
    private int gameId;
    private int tagId;
    private int tagValue;
    
    public AlterGame() {
        
    }
    
    public AlterGame(int gameId, int tagId, int tagValue) {
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
    public void forwardPacket(ClientListener listener) {
        listener.onAlterGame(this);
    }
    
    public int getGameId() {
        return gameId;
    }
    
    public int getTagId() {
        return tagId;
    }
    
    public int getTagValue() {
        return tagValue;
    }
    
}
