package com.sekwah.mira4j.config;

public interface SpawnFlag {
    int NONE = 0;
    int IS_CLIENT_CHARACTER = 1;
    
    public static String toString(int id) {
        switch(id) {
            case NONE: return "NONE";
            case IS_CLIENT_CHARACTER: return "IS_CLIENT_CHARACTER";
        }
        
        return null;
    }
}
