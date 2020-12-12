package com.sekwah.mira4j.network.inbound.packets.rpc;

import com.sekwah.mira4j.network.decoder.BufferObject;

public interface RPCObject extends BufferObject {
    int id();
}
