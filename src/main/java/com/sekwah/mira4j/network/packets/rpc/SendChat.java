package com.sekwah.mira4j.network.packets.rpc;

import java.util.Objects;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.RPCListener;
import com.sekwah.mira4j.utils.NonNull;

public class SendChat implements RPCMessage {
    private String message;
    
    public SendChat() {
        
    }
    
    private SendChat(String message) {
        this.message = message;
    }
    
    @Override
    public void read(PacketBuf reader) {
        message = reader.readString();
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeString(message);
    }
    
    @Override
    public int id() {
        return RPCType.SendChat.getId();
    }
    
    @Override
    public void forwardPacket(RPC rpc, RPCListener listener) {
        listener.onSendChat(rpc, this);
    }
    
    public String getMessage() {
        return message;
    }
    
    @Override
    public String toString() {
        return String.format("SendChat[ message=\"%s\" ]", message);
    }
    
    public static SendChat of(@NonNull String message) {
        return new SendChat(Objects.requireNonNull(message));
    }
}
