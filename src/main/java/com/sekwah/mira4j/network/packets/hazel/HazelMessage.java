package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.network.ClientListener;
import com.sekwah.mira4j.network.decoder.BufferObject;

public interface HazelMessage extends BufferObject {
    int id();
    void forwardPacket(ClientListener listener);
}
