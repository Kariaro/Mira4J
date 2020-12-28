package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.decoder.ClientInListener;

/**
 * @author HardCoded
 */
public interface HazelMessage {
    void read(PacketBuf reader);
    void write(PacketBuf writer);
    void forwardPacket(ClientInListener listener);
    int id();
    
    /**
     * @return
     */
    int getGameId();
    
    /**
     * Returns the player that sent this packet. If this packet was sent from the server
     * it will return <code>null</code>.
     * @return the player that sent this packet
     */
    Player getSender();
}
