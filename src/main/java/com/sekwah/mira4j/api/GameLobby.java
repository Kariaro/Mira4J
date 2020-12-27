package com.sekwah.mira4j.api;

import java.util.List;

import com.sekwah.mira4j.utils.NonNull;

public interface GameLobby {
    
    /**
     * Returns the game id of this lobby.
     * @return the game id of this lobby
     */
    int getGameId();
    
    /**
     * Returns the nummber of players in this lobby.<br>
     * 
     * @apiNote This method might return a different number of players than
     *          {@link #getPlayers()} because of threading.
     * @return the nummber of players in this lobby
     */
    int getNumPlayers();
    
    /**
     * Returns the host player of this lobby.
     * @return the host player of this lobby
     */
    Player getHost();
    
    /**
     * Returns a list of all players in this lobby.
     * @return a list of all players in this lobby
     */
    List<Player> getPlayers();
    
    /**
     * Returns the scene of this lobby.
     * @return the scene of this lobby
     */
    @NonNull Scene getScene();
    
    /**
     * Returns the millisecond time this lobby was created.
     * @return the millisecond time this lobby was created
     */
    long getCreationTime();
    
    /**
     * Returns <code>true</code> if this lobby has expired.
     * @return <code>true</code> if this lobby has expired
     */
    boolean hasExpired();
}
