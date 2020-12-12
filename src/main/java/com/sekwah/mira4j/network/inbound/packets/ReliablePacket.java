package com.sekwah.mira4j.network.inbound.packets;

import java.util.ArrayList;
import java.util.List;

import com.sekwah.mira4j.network.Packet;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.decoder.HazelDecoder;
import com.sekwah.mira4j.network.decoder.HazelMessage;

public class ReliablePacket implements Packet<ClientListener> {
    private int nonce;
    private List<HazelMessage> messages;
    
    public ReliablePacket() {
        
    }
    
    public ReliablePacket(int nonce, HazelMessage... args) {
        this.nonce = nonce;
        messages = new ArrayList<>();
        
        for(HazelMessage msg : args) {
            messages.add(msg);
        }
    }
    
    @Override
    public void readData(PacketBuf reader) {
        nonce = reader.readUnsignedShortBE();
        byte[] data = reader.readBytes(reader.readableBytes());
        
        PacketBuf wrap = PacketBuf.wrap(data);
        messages = new ArrayList<>();
        
        HazelMessage msg;
        while((msg = HazelDecoder.decode(wrap)) != null) {
            messages.add(msg);
        }
    }

    @Override
    public void writeData(PacketBuf writer) {
        writer.writeUnsignedShortBE(nonce);
        for(HazelMessage msg : messages) {
            msg.writeData(writer);
        }
    }

    @Override
    public void forwardPacket(ClientListener listener) {
        listener.onReliablePacket(this);
    }
    
    public int getNonce() {
        return nonce;
    }
    
    public List<HazelMessage> getMessages() {
        return messages;
    }
}
