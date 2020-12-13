package com.sekwah.mira4j.game;

public class Player {
    private boolean isImposter;
    private String name;
    private int clientId;
    
    public Player(int clientId) {
        this.clientId = clientId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isImposter() {
        return isImposter;
    }
    
    public int getClientId() {
        return clientId;
    }
}
