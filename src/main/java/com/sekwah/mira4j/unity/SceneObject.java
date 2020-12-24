package com.sekwah.mira4j.unity;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.packets.net.Component;
import com.sekwah.mira4j.utils.NonNull;

public interface SceneObject extends Component {
    /**
     * Return the scene of this object.
     * @return the scene of this object
     */
    @NonNull
    Scene getScene();
    
    /**
     * Return the netId of this object.
     * @return the netId of this object
     */
    int getNetId();
    
    /**
     * Deserialize the data contained in the reader and update this object
     */
    void deserialize(PacketBuf reader, boolean isSpawning);
    
    /**
     * Serialize the data contained in this object and write it to the writer
     */
    void serialize(PacketBuf writer, boolean isSpawning);
}
