package com.sekwah.mira4j.impl.unity;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.unity.Scene;
import com.sekwah.mira4j.unity.SceneObject;

public abstract class SceneObjectDB implements SceneObject {
    protected final Scene scene;
    protected final int netId;
    
    public SceneObjectDB(Scene scene, int netId) {
        this.scene = scene;
        this.netId = netId;
    }
    
    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public int getNetId() {
        return netId;
    }

    @Override
    public abstract void deserialize(PacketBuf reader, boolean isSpawning);
    
    @Override
    public abstract void serialize(PacketBuf writer, boolean isSpawning);
}
