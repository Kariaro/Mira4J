package com.sekwah.mira4j.network.inbound.packets;

import java.util.ArrayList;
import java.util.List;

import com.sekwah.mira4j.network.Packet;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.decoder.HazelDecoder;
import com.sekwah.mira4j.network.decoder.HazelMessage;

public class NormalPacket implements Packet<ClientListener> {
    private List<HazelMessage> messages;
    
    public NormalPacket() {
        messages = new ArrayList<>();
    }
    
    @Override
    public void readData(PacketBuf reader) {
        while(reader.readableBytes() > 0) {
            HazelMessage msg = HazelDecoder.decode(reader);
            if(msg == null) break;
            messages.add(msg);
        }
    }

    @Override
    public void writeData(PacketBuf writer) {}

    @Override
    public void forwardPacket(ClientListener listener) {
        listener.onNormalPacket(this);
    }
    
    public List<HazelMessage> getMessages() {
        return messages;
    }
}
