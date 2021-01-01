package com.sekwah.mira4j.network.packets.net;

import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.NetType;
import com.sekwah.mira4j.network.decoder.NetListener;

public class PlayerControl extends ComponentDB {
    private int playerId;
    private boolean isNew = true;
    
    public PlayerControl() {
        
    }
    
    public PlayerControl(int netId, int playerId, boolean isNew) {
        this.netId = netId;
        this.playerId = playerId;
        this.isNew = isNew;
    }
    
    @Override
    public void load(Scene scene, Player player) {
        super.load(scene, player);
        playerId = player.getPlayerId();
    }
    
    @Override
    public void read(PacketBuf reader, boolean isSpawning) {
        netId = reader.readUnsignedPackedInt();
        
        if (isSpawning) {
            reader = reader.readMessage();
            // isNew = reader.readBoolean();
            
            if (reader.readableBytes() == 0) {
                reader.release();
                return;
            }
        }
        
        playerId = reader.readUnsignedByte();
        
        if (isSpawning) {
            reader.release();
        }
    }
    
    @Override
    public void write(PacketBuf writer, boolean isSpawning) {
        writer.writeUnsignedPackedInt(netId);
        
        if (isSpawning) {
            writer.startMessage(0);
            writer.writeBoolean(isNew);
        }
        
        writer.writeByte(playerId);
        
        if (isSpawning) {
            writer.endMessage();
        }
    }

    @Override
    public int id() {
        return NetType.PlayerControl.getId();
    }
    
    @Override
    public void forwardPacket(NetListener listener) {
        listener.onPlayerControl(this);
    }
    
    public boolean isNew() {
        return isNew;
    }
    
    public int getPlayerId() {
        return playerId;
    }
    
    @Override
    public String toString() {
        return String.format("PlayerControl[ netId=%d, isNew=%s, playerId=%d ]", netId, isNew, playerId);
    }
}
