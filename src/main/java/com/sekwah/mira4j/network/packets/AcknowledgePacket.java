package com.sekwah.mira4j.network.packets;

import com.sekwah.mira4j.network.Packet;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.decoder.ClientInListener;

public class AcknowledgePacket implements Packet<ClientInListener> {
    private int nonce;
    private int missing_packets;
    
    public AcknowledgePacket() {
        
    }
    
    public AcknowledgePacket(int nonce, int missing_packets) {
        this.nonce = nonce;
        this.missing_packets = missing_packets;
    }
    
    @Override
    public void readData(PacketBuf reader) {
        nonce = reader.readUnsignedShortBE();
        missing_packets = reader.readUnsignedByte();
    }

    @Override
    public void writeData(PacketBuf writer) {
        writer.writeShortBE(nonce);
        writer.writeByte(missing_packets);
    }

    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onAcknowledgePacket(this);
    }
    
    public int getNonce() {
        return nonce;
    }
    
    public int getMissingPackets() {
        return missing_packets;
    }
}
