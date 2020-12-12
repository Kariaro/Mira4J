package com.sekwah.mira4j.game;

public class Player {
    private boolean isImposter = false;
    private String username;
    private int clientId;
    
    public Player(int clientId) {
        this.clientId = clientId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public boolean isImposter() {
        return isImposter;
    }
    
    public int getClientId() {
        return clientId;
    }
}
