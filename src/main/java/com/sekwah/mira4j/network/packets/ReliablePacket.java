package com.sekwah.mira4j.network.packets;

import java.util.ArrayList;
import java.util.List;

import com.sekwah.mira4j.network.Packet;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.decoder.ClientInListener;
import com.sekwah.mira4j.network.packets.hazel.Hazel;
import com.sekwah.mira4j.network.packets.hazel.HazelMessage;

public class ReliablePacket implements Packet<ClientInListener> {
    private int nonce;
    private byte[] data;
    private List<HazelMessage> messages;
    
    public ReliablePacket() {
        
    }
    
    public ReliablePacket(int nonce, HazelMessage... args) {
        this.nonce = nonce;
        messages = new ArrayList<>();
        
        for (HazelMessage msg : args) {
            messages.add(msg);
        }
    }
    
    @Override
    public void readData(PacketBuf reader) {
        nonce = reader.readUnsignedShortBE();
        //messages = new ArrayList<>();
        
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
        writer.writeUnsignedShortBE(nonce);
        for (HazelMessage msg : messages) {
            Hazel.write(writer, msg);
        }
    }

    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onReliablePacket(this);
    }
    
    public int getNonce() {
        return nonce;
    }
    
    public byte[] getData() {
        return data;
    }
    
//    public List<HazelMessage> getMessages() {
//        return messages;
//    }
    
    @Override
    public String toString() {
        return messages.toString();
    }
}
