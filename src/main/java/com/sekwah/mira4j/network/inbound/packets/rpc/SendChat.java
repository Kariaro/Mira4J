package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets;

public class SendChat implements RPCObject {
    private String message;
    
    public SendChat() {
        
    }
    
    public SendChat(String message) {
        this.message = message;
    }
    
    public void read(PacketBuf reader) {
        message = reader.readString();
    }
    
    public void write(PacketBuf writer) {
        writer.writeString(message);
    }
    
    public int id() {
        return Packets.RPCType.SendChat.getId();
    }
    
    public String getMessage() {
        return message;
    }
    
    @Override
    public String toString() {
        return String.format("SendChat[ message=\"%s\" ]", message);
    }
}