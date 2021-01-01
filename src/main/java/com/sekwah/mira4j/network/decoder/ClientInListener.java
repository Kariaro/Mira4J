package com.sekwah.mira4j.network.decoder;

import com.sekwah.mira4j.network.PacketListener;
import com.sekwah.mira4j.network.packets.*;
import com.sekwah.mira4j.network.packets.hazel.*;

public interface ClientInListener extends PacketListener, RPCListener, NetListener {
    // Hazel packets
    void onHelloPacket(HelloPacket packet);
    void onDisconnectPacket(DisconnectPacket packet);
    void onReliablePacket(ReliablePacket packet);
    void onNormalPacket(NormalPacket packet);
    void onAcknowledgePacket(AcknowledgePacket packet);
    void onKeepAlivePacket(PingPacket packet);
    
    // Message packets
    void onAlterGame(AlterGame packet);
    void onEndGame(EndGame packet);
    void onGameData(GameData packet);
    void onGameDataTo(GameDataTo packet);
    // void onGetGameList(GetGameList packet);
    void onHostGame(HostGame packet);
    void onJoinedGame(JoinedGame packet);
    void onJoinGame(JoinGame packet);
    void onKickPlayer(KickPlayer packet);
    void onRemoveGame(RemoveGame packet);
    void onRemovePlayer(RemovePlayer packet);
    void onStartGame(StartGame packet);
    void onWaitForHost(WaitForHost packet);
    void onRedirect(Redirect packet);
    // RedirectServer
    // GetGameListV2
}