package com.sekwah.mira4j.network.packets.hazel;

import java.util.ArrayList;
import java.util.List;

import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.impl.unity.GameManager;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.GameDataType;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.decoder.ClientInListener;
import com.sekwah.mira4j.network.packets.gamedata.*;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage.Despawn;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage.Ready;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage.SceneChange;
import com.sekwah.mira4j.network.packets.rpc.RPC;

public class GameData implements HazelMessage {
    private Scene scene;
    private int gameId;
    private List<GameDataMessage> messages;
    private boolean isSpawning;
    
    public GameData() {
        
    }
    
    public GameData(Scene scene, List<GameDataMessage> messages) {
        this.scene = scene;
        this.gameId = scene.getGameId();
        this.messages = messages;
    }
    
    public GameData(Scene scene, GameDataMessage... messages) {
        this(scene.getGameId(), messages);
        this.scene = scene;
    }
    
    public GameData(int gameId, GameDataMessage... messages) {
        this.gameId = gameId;
        this.messages = new ArrayList<>();
        for (GameDataMessage msg : messages) {
            this.messages.add(msg);
        }
    }
    
    @Override
    public void read(PacketBuf reader) {
        gameId = reader.readInt();
        Scene scene = GameManager.INSTANCE.getScene(gameId);
        
        messages = new ArrayList<>();
        
        GameDataMessage msg;
        while ((msg = GameDataDecoder.decode(scene, reader, isSpawning)) != null) {
            messages.add(msg);
        }
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeInt(gameId);
        
        for (GameDataMessage msg : messages) {
            writer.startMessage(msg.id());
            
            switch (GameDataType.fromId(msg.id())) {
                case Despawn: {
                    writer.writeUnsignedPackedInt(((Despawn)msg).net_id);
                    break;
                }
                case Data: {
                    msg.write(writer, isSpawning);
                    break;
                }
                case Spawn: {
                    msg.write(writer, isSpawning);
                    break;
                }
                case RPC: {
                    ((RPC)msg).write(writer);
                    break;
                }
                case Ready: {
                    writer.writeUnsignedPackedInt(((Ready)msg).client_id);
                    break;
                }
                case SceneChange: {
                    SceneChange pck = (SceneChange)msg;
                    writer.writeUnsignedPackedInt(pck.client_id);
                    writer.writeString(pck.scene);
                    break;
                }
                default:
                    break;
            }
            
            writer.endMessage();
        }
    }
    
    @Override
    public int id() {
        return HazelType.GameData.getId();
    }
    
    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onGameData(this);
    }
    
    public int getGameId() {
        return gameId;
    }
    
    public Scene getScene() {
        return scene;
    }
    
    public List<GameDataMessage> getMessages() {
        return messages;
    }
    
    @Override
    public String toString() {
        return messages.toString();
    }
}
