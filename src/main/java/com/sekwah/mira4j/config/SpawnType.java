package com.sekwah.mira4j.config;

public enum SpawnType {
    SHIP_STATUS(0),
    MEETING_HUD(1),
    LOBBY_BEHAVIOUR(2),
    GAME_DATA(3),
    PLAYER_CONTROL(4),
    HEADQUARTERS(5),
    PLANET_MAP(6),
    APRIL_SHIP_STATUS(7);
    
    final int id;
    private SpawnType(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }

    public static SpawnType fromId(int id) {
        for (SpawnType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        
        return null;
    }
}