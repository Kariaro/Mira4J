package com.sekwah.mira4j.network.packets.hazel;

import java.util.ArrayList;
import java.util.List;

import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.impl.unity.GameManager;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.GameDataType;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.decoder.ClientInListener;
import com.sekwah.mira4j.network.packets.gamedata.GameDataDecoder;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage.Despawn;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage.Ready;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage.SceneChange;
import com.sekwah.mira4j.network.packets.rpc.RPC;

public class GameDataTo implements HazelMessage {
    private int gameId;
    private int targetClientId;
    private List<GameDataMessage> messages;
    private boolean isSpawning;
    
    public GameDataTo() {
        
    }
    
    public GameDataTo(Scene scene, int targetClientId, List<GameDataMessage> messages) {
        this.gameId = scene.getGameId();
        this.targetClientId = targetClientId;
        this.messages = messages;
    }
    
    public GameDataTo(Scene scene, int targetClientId, GameDataMessage... messages) {
        this(scene.getGameId(), targetClientId, messages);
    }
    
    public GameDataTo(int gameId, int targetClientId, GameDataMessage... messages) {
        this.gameId = gameId;
        this.targetClientId = targetClientId;
        this.messages = new ArrayList<>();
        for(GameDataMessage msg : messages) {
            this.messages.add(msg);
        }
    }
    
    @Override
    public void read(PacketBuf reader) {
        gameId = reader.readInt();
        Scene scene = GameManager.getScene(gameId);
        
        targetClientId = reader.readUnsignedPackedInt();
        messages = new ArrayList<>();
        GameDataMessage msg;
        while((msg = GameDataDecoder.decode(scene, reader, isSpawning)) != null) {
            messages.add(msg);
        }
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeInt(gameId);
        writer.writeUnsignedPackedInt(targetClientId);
        
        for(GameDataMessage msg : messages) {
            writer.startMessage(msg.id());
            
            switch(GameDataType.fromId(msg.id())) {
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
        return HazelType.GameDataTo.getId();
    }
    
    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onGameDataTo(this);
    }
    
    public int getGameId() {
        return gameId;
    }
    
    public int getTargetClientId() {
        return targetClientId;
    }
    
    public List<GameDataMessage> getMessages() {
        return messages;
    }
    
    @Override
    public String toString() {
        return messages.toString();
    }
}
