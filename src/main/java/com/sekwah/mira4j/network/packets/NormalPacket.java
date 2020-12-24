package com.sekwah.mira4j.network.packets;

import java.util.ArrayList;
import java.util.List;

import com.sekwah.mira4j.network.ClientListener;
import com.sekwah.mira4j.network.Packet;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.packets.hazel.Hazel;
import com.sekwah.mira4j.network.packets.hazel.HazelMessage;

public class NormalPacket implements Packet<ClientListener> {
    private List<HazelMessage> messages;
    
    public NormalPacket() {
        messages = new ArrayList<>();
    }
    
    @Override
    public void readData(PacketBuf reader) {
        HazelMessage msg;
        while ((msg = Hazel.read(reader)) != null) {
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
