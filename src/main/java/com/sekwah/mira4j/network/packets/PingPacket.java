package com.sekwah.mira4j.network.packets;

import com.sekwah.mira4j.network.Packet;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.decoder.ClientInListener;

public class PingPacket implements Packet<ClientInListener> {
    private int nonce;
    
    public PingPacket() {
        
    }
    
    public PingPacket(int nonce) {
        this.nonce = nonce;
    }
    
    @Override
    public void readData(PacketBuf reader) {
        nonce = reader.readUnsignedShortBE();
    }

    @Override
    public void writeData(PacketBuf writer) {
        writer.writeShortBE(nonce);
    }

    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onKeepAlivePacket(this);
    }
    
    public int getNonce() {
        return nonce;
    }
}
