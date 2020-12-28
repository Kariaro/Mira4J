package com.sekwah.mira4j.network.packets;

import java.util.ArrayList;
import java.util.List;

import com.sekwah.mira4j.network.Packet;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.decoder.ClientInListener;
import com.sekwah.mira4j.network.packets.hazel.HazelMessage;

public class NormalPacket implements Packet<ClientInListener> {
    private List<HazelMessage> messages;
    private byte[] data;
    
    public NormalPacket() {
        messages = new ArrayList<>();
    }
    
    public NormalPacket(HazelMessage... messages) {
        this.messages = new ArrayList<>();
        for(HazelMessage message : messages) {
            this.messages.add(message);
        }
    }
    
    @Override
    public void readData(PacketBuf reader) {
        //reader.markReaderIndex();
        data = reader.readBytes(reader.readableBytes());
        //reader.resetReaderIndex();
        
//        HazelMessage msg;
//        while ((msg = Hazel.read(reader)) != null) {
//            messages.add(msg);
//        }
    }

    @Override
    public void writeData(PacketBuf writer) {
        for (HazelMessage msg : messages) {
            msg.write(writer);
        }
    }

    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onNormalPacket(this);
    }
    
    public byte[] getData() {
        return data;
    }
    
//    public List<HazelMessage> getMessages() {
//        return messages;
//    }
}
