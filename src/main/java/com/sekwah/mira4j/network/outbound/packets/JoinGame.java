package com.sekwah.mira4j.network.outbound.packets;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.MessageType;
import com.sekwah.mira4j.network.decoder.HazelMessage;
import com.sekwah.mira4j.network.inbound.packets.ClientListener;

public class JoinGame extends HazelMessage {
    private int gameId;
    private byte ownedMaps;
    private int clientId;
    private int hostId;
    
    public JoinGame() {
        super(MessageType.JoinGame);
    }
    
    public JoinGame(int gameId, int clientId, int hostId) {
        super(MessageType.JoinGame);
        this.gameId = gameId;
        this.clientId = clientId;
        this.hostId = hostId;
    }
    
    @Override
    public void readData(PacketBuf reader) {
        gameId = reader.readInt();
        ownedMaps = reader.readByte();
    }
    
    @Override
    public void writeData0(PacketBuf writer) {
        writer.writeInt(gameId);
        writer.writeUnsignedInt(clientId);
        writer.writeUnsignedInt(hostId);
    }
    
    @Override
    public void forwardPacket(ClientListener listener) {
        listener.onJoinGame(this);
    }
    
    public int getGameId() {
        return gameId;
    }
    
    public byte getOwnedMaps() {
        return ownedMaps;
    }
}
