package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.network.decoder.BufferObject;
import com.sekwah.mira4j.network.decoder.ClientInListener;

public interface HazelMessage extends BufferObject {
    int id();
    void forwardPacket(ClientInListener listener);
}
