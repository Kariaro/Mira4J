package com.sekwah.mira4j.network.decoder;

public interface GameDataMessage {
    public class Data implements GameDataMessage {
        public final int net_id;
        
        public Data(int net_id) {
            this.net_id = net_id;
        }
        
        public String toString() {
            return "Data { net_id=" + net_id + " }";
        }
    }
    
    // !!!! Spawn
    public class Spawn implements GameDataMessage {
        public final int spawn_type;
        public final int owner_id;
        
        public Spawn(int net_id) {
            this.spawn_type = net_id;
            this.owner_id = 0;
        }
        
        public String toString() {
            return "Spawn { spawn_type=" + spawn_type + ", owner_id=" + owner_id + " }";
        }
    }
    
    
    public class Despawn implements GameDataMessage {
        public final int net_id;
        
        public Despawn(int net_id) {
            this.net_id = net_id;
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
        
        public String toString() {
            return "ChangeScene { client_id=" + client_id + ", scene=\"" + scene + "\" }";
        }
    }
    
    public class Ready implements GameDataMessage {
        public int client_id;
        
        public Ready(int client_id) {
            this.client_id = client_id;
        }
        
        public String toString() {
            return "Ready { client_id=" + client_id + " }";
        }
    }
}