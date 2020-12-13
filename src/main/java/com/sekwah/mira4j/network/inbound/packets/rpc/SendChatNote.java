package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets;

public class SendChatNote implements RPCMessage {
    private int playerId;
    private int chatNoteType;
    
    public SendChatNote() {
        
    }
    
    public SendChatNote(int playerId, int chatNoteType) {
        this.playerId = playerId;
        this.chatNoteType = chatNoteType;
    }
    
    public void read(PacketBuf reader) {
        playerId = reader.readUnsignedByte();
        chatNoteType = reader.readUnsignedByte();
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedByte(playerId);
        writer.writeUnsignedByte(chatNoteType);
    }
    
    public int id() {
        return Packets.RPCType.SendChatNote.getId();
    }
    
    public int getPlayerId() {
        return playerId;
    }
    
    public int getChatNoteType() {
        return chatNoteType;
    }
    
    @Override
    public String toString() {
        return String.format("SendChatNote[ playerId=%d, chatNoteType=%d ]", playerId, chatNoteType);
    }
}
