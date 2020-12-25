package com.sekwah.mira4j.network.packets;

import com.sekwah.mira4j.network.Packet;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.decoder.ClientInListener;

public class HelloPacket implements Packet<ClientInListener> {
    private int nonce;
    private byte hazelVersion;
    private int version;
    private String username;
    
    @Override
    public void readData(PacketBuf reader) {
        nonce = reader.readUnsignedShortBE();
        hazelVersion = reader.readByte();
        version = reader.readInt();
        username = reader.readString();
    }

    @Override
    public void writeData(PacketBuf writer) {
        
    }

    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onHelloPacket(this);
    }

    public int getNonce() {
        return nonce;
    }
    
    public byte getHazelVersion() {
        return hazelVersion;
    }
    
    public int getVersion() {
        return version;
    }
    
    public String getUsername() {
        return username;
    }
}
