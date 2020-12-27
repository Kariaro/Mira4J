package com.sekwah.mira4j.network.packets.net;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.network.PacketBuf;

public interface Component {
    void read(PacketBuf reader, boolean isSpawning);
    void write(PacketBuf writer, boolean isSpawning);
    
    int getNetId();
    Scene getScene();
    Player getPlayer();
    
    int id();
}
