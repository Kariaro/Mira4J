package com.sekwah.mira4j.network.packets.rpc;

import java.util.Objects;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.utils.NonNull;

public class SendChat implements RPCMessage {
    private String message;
    
    public SendChat() {
        
    }
    
    private SendChat(String message) {
        this.message = message;
    }
    
    public void read(PacketBuf reader) {
        message = reader.readString();
    }
    
    public void write(PacketBuf writer) {
        writer.writeString(message);
    }
    
    public int id() {
        return RPCType.SendChat.getId();
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
