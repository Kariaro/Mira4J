package com.sekwah.mira4j.network.packets.rpc;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.decoder.RPCListener;

public interface RPCMessage {
    void read(PacketBuf reader);
    void write(PacketBuf writer);
    int id();
    void forwardPacket(RPC rpc, RPCListener listener);
}
