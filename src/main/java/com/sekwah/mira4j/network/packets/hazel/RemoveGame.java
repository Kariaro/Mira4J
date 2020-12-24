package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.config.DisconnectReason;
import com.sekwah.mira4j.network.ClientListener;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;

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
    
    @Override
    public void forwardPacket(ClientListener listener) {
        listener.onRemoveGame(this);
    }
}
