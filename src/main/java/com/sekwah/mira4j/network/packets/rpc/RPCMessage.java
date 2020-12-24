package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.decoder.BufferObject;

public interface RPCMessage extends BufferObject {
    int id();
}
