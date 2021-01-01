package com.sekwah.mira4j.network.decoder;

import com.sekwah.mira4j.network.packets.net.*;

public interface NetListener {
    void onLobbyBehaviour(LobbyBehaviour packet);
    void onNetGameData(NetGameData packet);
    void onVoteBanSystem(VoteBanSystem packet);
    void onPlayerControl(PlayerControl packet);
    void onPlayerPhysics(PlayerPhysics packet);
    void onCustomNetworkTransform(CustomNetworkTransform packet);
}
