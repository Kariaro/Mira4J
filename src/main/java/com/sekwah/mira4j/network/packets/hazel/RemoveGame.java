package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.config.DisconnectReason;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.decoder.ClientInListener;

/**
 * Server-to-Client
 */
public class RemoveGame implements HazelMessage {
    private DisconnectReason reason;
    
    public RemoveGame() {
        this(DisconnectReason.SERVER_REQUEST); // Default
    }
    
    public RemoveGame(DisconnectReason reason) {
        this.reason = reason;
    }
    
    @Override
    public void read(PacketBuf reader) {
        
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeByte(reason.getId());
    }
    
    @Override
    public int id() {
        return HazelType.RemoveGame.getId();
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
    
    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onRemoveGame(this);
    }
}
