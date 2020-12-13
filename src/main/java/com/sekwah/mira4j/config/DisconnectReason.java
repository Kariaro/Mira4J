package com.sekwah.mira4j.config;

public enum DisconnectReason {
    EXIT_GAME(0),
    GAME_FULL(1),
    GAME_STARTED(2),
    GAME_NOT_FOUND(3),
    CUSTOM_MESSAGE_1(4),
    INCORRECT_VERSION(5),
    BANNED(6),
    KICKED(7),
    CUSTOM(8),
    INVALID_NAME(9),
    HACKING(10),
    DESTROY(16),
    ERROR(17),
    INCORRECT_GAME(18),
    SERVER_REQUEST(19),
    SERVER_FULL(20),
    FOCUS_LOST_BACKGROUND(207),
    INTENTIONAL_LEAVING(208),
    FOCUS_LOST(209),
    NEW_CONNECTION(210),
    ;
    
    final int id;
    private DisconnectReason(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }

    public static DisconnectReason fromId(int id) {
        for (DisconnectReason reason : values()) {
            if (reason.id == id) {
                return reason;
            }
        }
        
        return null;
    }
}
