package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.decoder.ClientInListener;

/**
 * @deprecated Legacy message
 */
public class GetGameList implements HazelMessage {
    protected GetGameList() {
        
    }
    
    @Override
    public void read(PacketBuf reader) {}
    
    @Override
    public void write(PacketBuf writer) {}
    
    @Override
    public int id() {
        return HazelType.GetGameList.getId();
    }
    
    /**
     * This packet has no sender
     */
    @Deprecated
    public Player getSender() {
        return null;
    }
    
    /**
     * This packet has no game id
     */
    @Deprecated
    public int getGameId() {
        return 0;
    }
    
    @Override
    public void forwardPacket(ClientInListener listener) {}
}
