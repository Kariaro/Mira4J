package com.sekwah.mira4j.network.packets.net;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.decoder.NetListener;

public interface Component {
    void read(PacketBuf reader, boolean isSpawning);
    void write(PacketBuf writer, boolean isSpawning);
    void forwardPacket(NetListener listener);
    
    int getNetId();
    Scene getScene();
    Player getPlayer();
    
    int id();
}
