package com.sekwah.mira4j.network.packets.gamedata;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.GameDataType;

public interface GameDataMessage {
    public class Despawn implements GameDataMessage {
        public final int net_id;
        
        public Despawn(int net_id) {
            this.net_id = net_id;
        }
        
        public int id() {
            return GameDataType.Despawn.getId();
        }
        
        public String toString() {
            return "Despawn { net_id=" + net_id + " }";
        }
    }
    
    public class SceneChange implements GameDataMessage {
        public final int client_id;
        public final String scene;
        
        public SceneChange(int client_id, String scene) {
            this.client_id = client_id;
            this.scene = scene;
        }
        
        public int id() {
            return GameDataType.SceneChange.getId();
        }
        
        public String toString() {
            return "ChangeScene { client_id=" + client_id + ", scene=\"" + scene + "\" }";
        }
    }
    
    public class Ready implements GameDataMessage {
        public int client_id;
        
        public Ready(int client_id) {
            this.client_id = client_id;
        }
        
        public int id() {
            return GameDataType.Ready.getId();
        }
        
        public String toString() {
            return "Ready { client_id=" + client_id + " }";
        }
    }

    int id();
    default void read(PacketBuf reader, boolean isSpawning) {}
    default void write(PacketBuf writer, boolean isSpawning) {}
}