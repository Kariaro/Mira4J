package com.sekwah.mira4j.plugin.api;

import com.sekwah.mira4j.config.DisconnectReason;
import com.sekwah.mira4j.utils.NonNull;

public interface Player2 {
    @NonNull String getName();
    void setName(@NonNull String name);
    
    void disconnect(DisconnectReason reason, String message);
    void disconnect(DisconnectReason reason);
    void disconnect();
}
