package com.sekwah.mira4j.unity;

public interface SceneObject {
    /**
     * Return the scene of this object.
     * @return the scene of this object
     */
    Scene getScene();
    
    /**
     * Return the netId of this object.
     * @return the netId of this object
     */
    int getNetId();
}
