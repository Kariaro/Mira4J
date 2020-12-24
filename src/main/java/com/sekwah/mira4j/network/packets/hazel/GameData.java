package com.sekwah.mira4j.network.packets.hazel;

import java.util.ArrayList;
import java.util.List;

import com.sekwah.mira4j.game.GameLobby;
import com.sekwah.mira4j.network.ClientListener;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.GameDataType;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.packets.gamedata.*;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage.Despawn;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage.Ready;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage.SceneChange;
import com.sekwah.mira4j.network.packets.rpc.RPC;

public class GameData implements HazelMessage {
    private int gameId;
    private List<GameDataMessage> messages;
    private boolean isSpawning;
    
    public GameData() {
        
    }
    
    public GameData(boolean isSpawning) {
        this.isSpawning = isSpawning;
    }
    
    public GameData(int gameId, List<GameDataMessage> messages) {
        this.gameId = gameId;
        this.messages = messages;
    }
    
    public GameData(GameLobby lobby, GameDataMessage... messages) {
        this(lobby.getGameId(), messages);
    }
    
    public GameData(int gameId, GameDataMessage... messages) {
        this.gameId = gameId;
        this.messages = new ArrayList<>();
        for(GameDataMessage msg : messages) {
            this.messages.add(msg);
        }
    }
    
    @Override
    public void read(PacketBuf reader) {
        gameId = reader.readInt();
        messages = new ArrayList<>();
        GameDataMessage msg;
        while((msg = GameDataDecoder.decode(reader, isSpawning)) != null) {
            messages.add(msg);
        }
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeInt(gameId);
        
        for(GameDataMessage msg : messages) {
            switch(GameDataType.fromId(msg.id())) {
                case Despawn: {
                    PacketBuf buf = PacketBuf.create(4096);
                    buf.writeUnsignedPackedInt(((Despawn)msg).net_id);
                    byte[] bytes = buf.readBytes(buf.readableBytes());
                    buf.release();
                    
                    writer.writeShort(bytes.length);
                    writer.writeByte(GameDataType.Despawn.getId());
                    writer.writeBytes(bytes);
                    break;
                }
                
                case Data: {
                    writer.startMessage(msg.id());
                    msg.write(writer, isSpawning);
                    writer.endMessage();
                    break;
                }
                
                case Spawn: {
                    writer.startMessage(msg.id());
                    msg.write(writer, isSpawning);
                    writer.endMessage();
                    break;
                }
                
                case RPC: {
                    writer.writeByte(GameDataType.RPC.getId());
                    ((RPC)msg).write(writer);
                    break;
                }
                
                case Ready: {
                    PacketBuf buf = PacketBuf.create(4096);
                    buf.writeUnsignedPackedInt(((Ready)msg).client_id);
                    byte[] bytes = buf.readBytes(buf.readableBytes());
                    buf.release();
                    
                    writer.writeShort(bytes.length);
                    writer.writeByte(GameDataType.Ready.getId());
                    writer.writeBytes(bytes);
                    break;
                }
                
                case SceneChange: {
                    SceneChange pck = (SceneChange)msg;
                    
                    PacketBuf buf = PacketBuf.create(4096);
                    buf.writeUnsignedPackedInt(pck.client_id);
                    buf.writeString(pck.scene);
                    byte[] bytes = buf.readBytes(buf.readableBytes());
                    buf.release();
                    
                    writer.writeShort(bytes.length);
                    writer.writeByte(GameDataType.SceneChange.getId());
                    writer.writeBytes(bytes);
                    break;
                }
                
                default:
                    break;
            }
        }
    }
    
    @Override
    public int id() {
        return HazelType.GameData.getId();
    }
    @Override
    public void forwardPacket(ClientListener listener) {
        listener.onGameData(this);
    }
    
    public int getGameId() {
        return gameId;
    }
    
    public List<GameDataMessage> getMessages() {
        return messages;
    }
}