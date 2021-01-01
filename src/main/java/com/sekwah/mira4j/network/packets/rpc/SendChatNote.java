package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;

public class SendChatNote implements RPCMessage {
    private int playerId;
    private int chatNoteType;
    
    public SendChatNote() {
        
    }
    
    public SendChatNote(int playerId, int chatNoteType) {
        this.playerId = playerId;
        this.chatNoteType = chatNoteType;
    }
    
    @Override
    public void read(PacketBuf reader) {
        playerId = reader.readUnsignedByte();
        chatNoteType = reader.readUnsignedByte();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeUnsignedByte(playerId);
        writer.writeUnsignedByte(chatNoteType);
    }
    
    @Override
    public int id() {
        return RPCType.SendChatNote.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onSendChatNote(rpc, this);
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
