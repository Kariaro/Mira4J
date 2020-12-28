package com.sekwah.mira4j.network.packets.hazel;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.decoder.ClientInListener;

/**
 * Server-to-Client
 */
public class Redirect implements HazelMessage {
    private InetAddress addr;
    private int port;
    
    public Redirect() {
        
    }
    
    public Redirect(InetAddress addr, int port) {
        this.addr = addr;
        this.port = port;
    }
    
    @Override
    public void read(PacketBuf reader) {
        try {
            addr = InetAddress.getByAddress(reader.readBytes(4));
        } catch(UnknownHostException e) {
            e.printStackTrace();
        }
        
        port = reader.readUnsignedShort();
    }
    
    @Override
    public void write(PacketBuf writer) {
        if(addr == null) {
            writer.writeInt(0); // Write 4 bytes of zero
        } else {
            writer.writeBytes(addr.getAddress());
        }
        
        writer.writeUnsignedShort(port);
    }
    
    @Override
    public int id() {
        return HazelType.Redirect.getId();
    }
    
    /**
     * This packet has no sender
     */
    @Deprecated
    public Player getSender() {
        return null;
    }
    
    @Override
    public int getGameId() {
        return 0;
    }
    
    public InetAddress getAddress() {
        return addr;
    }
    
    public int getPort() {
        return port;
    }
    
    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onRedirect(this);
    }
}
