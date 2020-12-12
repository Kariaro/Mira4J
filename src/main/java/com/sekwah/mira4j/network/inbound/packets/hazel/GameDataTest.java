package com.sekwah.mira4j.network.inbound.packets.hazel;

import java.util.List;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.MessageType;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.GameDataMessage;
import com.sekwah.mira4j.network.decoder.HazelMessage;
import com.sekwah.mira4j.network.inbound.packets.ClientListener;

public class GameDataTest extends HazelMessage {
    private int gameId;
    private int clientId;
    private List<GameDataMessage> messages;
    private String message;
    
    public GameDataTest(int gameId, int clientId, List<GameDataMessage> messages) {
        super(MessageType.GameData);
        this.gameId = gameId;
        this.clientId = clientId;
        this.messages = messages;
    }
    
    private int testId;
    public GameDataTest(int gameId, int testId, int clientId, String message) {
        super(MessageType.GameData);
        this.gameId = gameId;
        this.testId = testId;
        this.clientId = clientId;
        this.message = message;
    }
    
    @Override
    public void readData(PacketBuf reader) {
        
    }
    
    @Override
    public void writeData0(PacketBuf writer) {
        writer.writeInt(gameId);
        writer.writeByte(0x02); // RCP
        writer.writeUnsignedPackedInt(testId); // Sender
        writer.writeByte(RPCType.SendChat.getId());
        writer.writeString(message);
    }

    @Override
    public void forwardPacket(ClientListener listener) {
    }
    
    public int getGameId() {
        return gameId;
    }
    
    public List<GameDataMessage> getMessages() {
        return messages;
    }
}
