package com.sekwah.mira4j.network.inbound.packets.hazel;

import java.util.ArrayList;
import java.util.List;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.GameDataType;
import com.sekwah.mira4j.network.Packets.MessageType;
import com.sekwah.mira4j.network.decoder.GameDataDecoder;
import com.sekwah.mira4j.network.decoder.GameDataMessage;
import com.sekwah.mira4j.network.decoder.GameDataMessage.Despawn;
import com.sekwah.mira4j.network.decoder.GameDataMessage.Ready;
import com.sekwah.mira4j.network.decoder.GameDataMessage.SceneChange;
import com.sekwah.mira4j.network.decoder.HazelMessage;
import com.sekwah.mira4j.network.inbound.packets.ClientListener;
import com.sekwah.mira4j.network.inbound.packets.rpc.RPC;

public class GameData extends HazelMessage {
    private int gameId;
    private List<GameDataMessage> messages;
    
    public GameData() {
        super(MessageType.GameData);
    }
    
    public GameData(int gameId, List<GameDataMessage> messages) {
        super(MessageType.GameData);
        this.gameId = gameId;
        this.messages = messages;
    }
    
    public GameData(int gameId, GameDataMessage... messages) {
        super(MessageType.GameData);
        this.gameId = gameId;
        this.messages = new ArrayList<>();
        for(GameDataMessage msg : messages) {
            this.messages.add(msg);
        }
    }
    
    @Override
    public void readData(PacketBuf reader) {
        this.gameId = reader.readInt();
        messages = new ArrayList<>();
        GameDataMessage msg;
        while((msg = GameDataDecoder.decode(reader)) != null) {
            messages.add(msg);
        }
    }
    
    @Override
    public void writeData0(PacketBuf writer) {
        writer.writeInt(gameId);
        
        for(GameDataMessage msg : messages) {
            if(msg instanceof RPC) { // 0x02
                writer.writeByte(GameDataType.RPC.getId());
                ((RPC)msg).write(writer);
            }
            
            if(msg instanceof Despawn) { // 0x05
                PacketBuf buf = PacketBuf.create(4096);
                buf.writeUnsignedPackedInt(((Despawn)msg).net_id);
                byte[] bytes = buf.readBytes(buf.readableBytes());
                buf.release();
                
                writer.writeShort(bytes.length);
                writer.writeByte(GameDataType.Despawn.getId());
                writer.writeBytes(bytes);
            }
            
            if(msg instanceof SceneChange) { // 0x06
                SceneChange pck = (SceneChange)msg;
                
                PacketBuf buf = PacketBuf.create(4096);
                buf.writeUnsignedPackedInt(pck.client_id);
                buf.writeString(pck.scene);
                byte[] bytes = buf.readBytes(buf.readableBytes());
                buf.release();
                
                writer.writeShort(bytes.length);
                writer.writeByte(GameDataType.SceneChange.getId());
                writer.writeBytes(bytes);
            }
            
            if(msg instanceof Ready) { // 0x07
                PacketBuf buf = PacketBuf.create(4096);
                buf.writeUnsignedPackedInt(((Ready)msg).client_id);
                byte[] bytes = buf.readBytes(buf.readableBytes());
                buf.release();
                
                writer.writeShort(bytes.length);
                writer.writeByte(GameDataType.Ready.getId());
                writer.writeBytes(bytes);
            }
            
            // ChangeSettings
        }
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