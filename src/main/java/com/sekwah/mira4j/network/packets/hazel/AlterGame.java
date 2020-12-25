package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.decoder.ClientInListener;

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
    public void forwardPacket(ClientInListener listener) {
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
