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
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage.SceneChange;
import com.sekwah.mira4j.utils.NonNull;

public class GameDataTo implements HazelMessage {
    private final Player sender;
    private int gameId;
    private int targetClientId;
    private List<GameDataMessage> messages;
    private boolean isSpawning;
    
    protected GameDataTo(Player sender) {
        this.sender = sender;
    }
    
    private GameDataTo(GameLobby lobby, int targetClientId, GameDataMessage... messages) {
        this.sender = null;
        this.gameId = lobby.getGameId();
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
        if(gameId != 0 && sender.getScene() != scene) {
            throw new InvalidPacketException("Invalid scene");
        }
        
        targetClientId = reader.readUnsignedPackedInt();
        messages = new ArrayList<>();
        GameDataMessage msg;
        while((msg = GameDataDecoder.decode(sender.getScene(), reader, isSpawning)) != null) {
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
                    msg.write(writer, isSpawning);
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
                    msg.write(writer, isSpawning);
                    break;
                }
                case Ready: {
                    msg.write(writer, isSpawning);
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
    public Player getSender() {
        return sender;
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
    
    public static GameDataTo of(@NonNull GameLobby lobby, @NonNull Player player, GameDataMessage... messages) {
        return new GameDataTo(lobby, player.getClientId(), messages);
    }
}
