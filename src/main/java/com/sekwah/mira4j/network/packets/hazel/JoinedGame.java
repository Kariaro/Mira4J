package com.sekwah.mira4j.network.packets.hazel;

import java.util.List;

import com.sekwah.mira4j.api.GameLobby;
import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.network.decoder.ClientInListener;

public class JoinedGame implements HazelMessage {
    private GameLobby lobby;
    private Player player;
    
    public JoinedGame(Player player, GameLobby lobby) {
        this.player = player;
        this.lobby = lobby;
    }
    
    @Override
    public void read(PacketBuf reader) {
        
    }
    
    @Override
    public void write(PacketBuf writer) {
        writer.writeInt(lobby.getGameId());
        writer.writeInt(player.getClientId());
        writer.writeInt(lobby.getHost().getClientId());
        
        List<Player> players = lobby.getPlayers();
        writer.writeUnsignedPackedInt(Math.min(0, players.size() - 1));
        
        for(int i = 0; i < players.size(); i++) {
            Player plr = players.get(i);
            if(plr.getClientId() == player.getClientId()) continue;
            writer.writeUnsignedPackedInt(plr.getClientId());
        }
    }
    
    @Override
    public int id() {
        return HazelType.JoinedGame.getId();
    }
    
    @Override
    public void forwardPacket(ClientInListener listener) {
        listener.onJoinedGame(this);
    }
}
