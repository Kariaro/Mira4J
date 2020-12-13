package com.sekwah.mira4j.network.inbound.packets.hazel;

import com.sekwah.mira4j.network.ClientListener;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;

/**
 * @deprecated Legacy message
 */
public class GetGameList implements HazelMessage {
    @Override
    public void read(PacketBuf reader) {}
    
    @Override
    public void write(PacketBuf writer) {}
    
    @Override
    public int id() {
        return HazelType.GetGameList.getId();
    }
    
    @Override
    public void forwardPacket(ClientListener listener) {}
}
