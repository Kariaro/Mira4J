package com.sekwah.mira4j.network.packets.net;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.api.Scene;

public abstract class ComponentDB implements Component {
    protected Scene scene;
    protected Player player;
    protected int netId;
    
    public void load(Scene scene, Player player) {
        this.scene = scene;
        this.player = player;
    }
    
    public void setNetId(int netId) {
        this.netId = netId;
    }
    
    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public int getNetId() {
        return netId;
    }
}
