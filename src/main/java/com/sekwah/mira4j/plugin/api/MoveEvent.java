package com.sekwah.mira4j.plugin.api;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.config.Vector2;

public class MoveEvent extends Event {
    private Vector2 location;
    private Vector2 velocity;
    
    public MoveEvent(Player sender, Vector2 location, Vector2 velocity) {
        super(sender);
        this.location = location;
        this.velocity = velocity;
    }
    
    public Vector2 getLocation() {
        return location.clone();
    }
    
    public Vector2 getVelocity() {
        return velocity.clone();
    }
    
    @Override
    public String toString() {
        return String.format("<%s>: location=%s velocity=%s", player, location, velocity);
    }
}
