package com.sekwah.mira4j.network.packets.hazel;

import java.util.ArrayList;
import java.util.List;

import com.sekwah.mira4j.api.GameLobby;
import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.impl.unity.GameManager;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.GameDataType;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.decoder.ClientInListener;
import com.sekwah.mira4j.network.error.InvalidPacketException;
import com.sekwah.mira4j.network.packets.gamedata.GameDataDecoder;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage.Despawn;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage.Ready;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage.SceneChange;
import com.sekwah.mira4j.network.packets.rpc.RPC;

public class GameData implements HazelMessage {
    private final Player sender;
    private int gameId;
    private List<GameDataMessage> messages;
    private boolean isSpawning;
    
    protected GameData(Player sender) {
        this.sender = sender;
    }
    
    private GameData(GameLobby lobby, GameDataMessage... messages) {
        this.sender = null;
        this.gameId = lobby.getGameId();
        this.messages = new ArrayList<>();
        for (GameDataMessage msg : messages) {
            this.messages.add(msg);
        }
    }
    
    @Override
    public void read(PacketBuf reader) {
        gameId = reader.readInt();
        Scene scene = GameManager.getScene(gameId);
        if(gameId != 0 && sender.getScene() != scene) {
            throw new InvalidPacketException("Invalid scene");
        }
        
        messages = new ArrayList<>();
        
        // Always use the scene of the player
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
    public Player getSender() {
        return sender;
    }
    
    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onGameData(this);
    }
    
    public int getGameId() {
        return gameId;
    }
    
    public List<GameDataMessage> getMessages() {
        return messages;
    }
    
    @Override
    public String toString() {
        return messages.toString();
    }
    
    public static GameData of(GameLobby lobby, GameDataMessage... messages) {
        return new GameData(lobby, messages);
    }
}
