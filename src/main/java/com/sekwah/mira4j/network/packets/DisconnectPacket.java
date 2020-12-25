package com.sekwah.mira4j.network.packets;

import com.sekwah.mira4j.config.DisconnectReason;
import com.sekwah.mira4j.network.Packet;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.decoder.ClientInListener;

public class DisconnectPacket implements Packet<ClientInListener> {
    private DisconnectReason reason;
    private String message;
    
    public DisconnectPacket() {
        
    }

    public DisconnectPacket(DisconnectReason reason) {
        this(reason, null);
    }
    
    public DisconnectPacket(DisconnectReason reason, String message) {
        this.reason = reason;
        this.message = message;
    }
    
    @Override
    public void readData(PacketBuf reader) {
        if (reader.readableBytes() == 0) return;
        
        if (reader.readUnsignedByte() == 1) {
            reader = reader.readMessage();
            
            try {
                reason = DisconnectReason.fromId(reader.readUnsignedByte());
                
                if (reader.readableBytes() > 0) {
                    message = reader.readString();
                }
            } finally {
                reader.release();
            }
        }
    }

    @Override
    public void writeData(PacketBuf writer) {
        if(reason != null) {
            writer.writeUnsignedByte(1);
            writer.startMessage(0);
            writer.writeUnsignedByte(reason.getId());
            
            if(message != null) {
                writer.writeString(message);
            }
            
            writer.endMessage();
        }
    }
    
    public DisconnectReason getDisconnectReason() {
        return reason;
    }
    
    public String getMessage() {
        return message;
    }
    
    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onDisconnectPacket(this);
    }
}
