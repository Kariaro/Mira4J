package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.decoder.BufferObject;

public interface RPCMessage extends BufferObject {
    int id();
}
