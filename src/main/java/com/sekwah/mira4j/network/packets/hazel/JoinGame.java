package com.sekwah.mira4j.network.packets.hazel;

import com.sekwah.mira4j.config.DisconnectReason;
import com.sekwah.mira4j.network.ClientListener;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;

public class JoinGame implements HazelMessage {
    private int gameId;
    private byte ownedMaps;
    private int clientId;
    private int hostId;
    private DisconnectReason reason;
    
    public JoinGame() {
        
    }
    
    public JoinGame(int gameId, int clientId, int hostId) {
        this.gameId = gameId;
        this.clientId = clientId;
        this.hostId = hostId;
    }
    
    public JoinGame(DisconnectReason reason) {
        this.reason = reason;
    }
    
    @Override
    public void read(PacketBuf reader) {
        gameId = reader.readInt();
        ownedMaps = reader.readByte();
    }
    
    @Override
    public void write(PacketBuf writer) {
        if(reason != null) {
            writer.writeByte(reason.getId());
        } else {
            writer.writeInt(gameId);
            writer.writeUnsignedInt(clientId);
            writer.writeUnsignedInt(hostId);   
        }
    }
    
    @Override
    public int id() {
        return HazelType.JoinGame.getId();
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
