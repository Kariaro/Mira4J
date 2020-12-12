package com.sekwah.mira4j.network.decoder;

import com.sekwah.mira4j.network.PacketBuf;

public interface BufferObject {
    void read(PacketBuf reader);
    void write(PacketBuf writer);
}
