package com.sekwah.mira4j.network.outbound.packets;

import com.sekwah.mira4j.game.Lobby;
import com.sekwah.mira4j.game.Player;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.MessageType;
import com.sekwah.mira4j.network.decoder.HazelMessage;

public class JoinedGame extends HazelMessage {
    private Lobby lobby;
    private Player player;
    
    public JoinedGame(Player player, Lobby lobby) {
        super(MessageType.JoinedGame);
        this.player = player;
        this.lobby = lobby;
    }
    
    @Override
    public void readData(PacketBuf reader) {
        
    }
    
    @Override
    public void writeData0(PacketBuf writer) {
        writer.writeInt(lobby.getGameId());
        writer.writeInt(player.getClientId());
        writer.writeInt(lobby.getHost().getClientId());
        
        Player[] players = lobby.getPlayers();
        writer.writeUnsignedPackedInt(Math.min(0, players.length - 1));
        
        for(int i = 0; i < players.length; i++) {
            Player plr = players[i];
            if(plr.getClientId() == player.getClientId()) continue;
            writer.writeUnsignedPackedInt(plr.getClientId());
        }
    }
}
