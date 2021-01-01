package com.sekwah.mira4j.config;

import com.sekwah.mira4j.api.Player;

public class PlayerInfo {
    public String name;
    public int playerId;
    public int colorId;
    public int hatId;
    public int petId;
    public int skinId;
    public int flags;
    public TaskInfo[] tasks = new TaskInfo[0];
    
    public PlayerInfo() {
        
    }
    
    public PlayerInfo(Player player) {
        name = player.getName();
        playerId = player.getPlayerId();
        colorId = player.getColorId();
        hatId = player.getHatId();
        petId = player.getPetId();
        skinId = player.getSkinId();
        flags = player.getFlags();
    }
    
    public PlayerInfo(int playerId, String name, int colorId, int hatId, int petId, int skinId, int flags) {
        this.playerId = playerId;
        this.name = name;
        this.colorId = colorId;
        this.hatId = hatId;
        this.petId = petId;
        this.skinId = skinId;
        this.flags = flags;
    }
    
    @Override
    public String toString() {
        return String.format("<id=%d name='%s'>", playerId, name);
    }
}
