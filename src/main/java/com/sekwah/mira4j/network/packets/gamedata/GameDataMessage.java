package com.sekwah.mira4j.network.packets.gamedata;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.GameDataType;
import com.sekwah.mira4j.network.decoder.ClientInListener;

public interface GameDataMessage {
    public class Despawn implements GameDataMessage {
        private int net_id;
        
        @Override
        public int id() {
            return GameDataType.Despawn.getId();
        }
        
        @Override
        public void read(PacketBuf reader, boolean isSpawning) {
            net_id = reader.readUnsignedPackedInt();
        }
        
        @Override
        public void write(PacketBuf writer, boolean isSpawning) {
            writer.writeUnsignedPackedInt(net_id);
        }
        
        @Override
        public void forwardPacket(ClientInListener listener) {
            // FIXME: Implement this
        }
        
        public int getNetId() {
            return net_id;
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
        
        @Override
        public int id() {
            return GameDataType.SceneChange.getId();
        }
        
        @Override
        public void forwardPacket(ClientInListener listener) {
            // FIXME: Implement this
        }
        
        public String toString() {
            return "SceneChange { client_id=" + client_id + ", scene=\"" + scene + "\" }";
        }
    }
    
    /**
     * Client-to-Host
     */
    public class Ready implements GameDataMessage {
        private int client_id;
        
        @Override
        public int id() {
            return GameDataType.Ready.getId();
        }
        
        @Override
        public void read(PacketBuf reader, boolean isSpawning) {
            client_id = reader.readUnsignedPackedInt();
        }
        
        @Override
        public void write(PacketBuf writer, boolean isSpawning) {
            writer.writeUnsignedPackedInt(client_id);
        }
        
        @Override
        public void forwardPacket(ClientInListener listener) {
            // FIXME: Implement this
        }
        
        public int getClientId() {
            return client_id;
        }
        
        public String toString() {
            return "Ready { client_id=" + client_id + " }";
        }
    }

    int id();
    void forwardPacket(ClientInListener listener);
    default void read(PacketBuf reader, boolean isSpawning) {}
    default void write(PacketBuf writer, boolean isSpawning) {}
}