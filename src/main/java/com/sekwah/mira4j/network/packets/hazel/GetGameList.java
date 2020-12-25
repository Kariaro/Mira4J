package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.decoder.ClientInListener;

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
    public void forwardPacket(ClientInListener listener) {}
}
