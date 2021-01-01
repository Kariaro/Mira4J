package com.sekwah.mira4j.plugin.api;

import com.sekwah.mira4j.api.GameLobby;
import com.sekwah.mira4j.api.Player;

public class Event {
    protected GameLobby lobby;
    protected Player player;
    protected boolean canceled = false;
    
    protected Event(Player player) {
        this.player = player;
        this.lobby = player.getLobby();
    }
    
    public GameLobby getLobby() {
        return lobby;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public void cancel() {
        canceled = true;
    }
    
    public boolean isCanceled() {
        return canceled;
    }
}
