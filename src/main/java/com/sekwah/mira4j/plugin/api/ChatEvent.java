package com.sekwah.mira4j.plugin.api;

import com.sekwah.mira4j.api.Player;

public class ChatEvent extends Event {
    private String message;
    
    public ChatEvent(Player sender, String message) {
        super(sender);
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
    
    @Override
    public String toString() {
        return String.format("<%s>: %s", player, message);
    }
}
