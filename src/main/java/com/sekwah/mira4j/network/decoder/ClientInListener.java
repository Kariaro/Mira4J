package com.sekwah.mira4j.network.decoder;

import com.sekwah.mira4j.network.PacketListener;
import com.sekwah.mira4j.network.packets.*;
import com.sekwah.mira4j.network.packets.hazel.*;
import com.sekwah.mira4j.network.packets.rpc.*;

public interface ClientInListener extends PacketListener {
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
    
    
    // RPC
    void onAddVote(AddVote packet);
    void onCastVote(CastVote packet);
    void onCheckColor(CheckColor packet);
    void onCheckName(CheckName packet);
    void onClearVote(ClearVote packet);
    void onClose(Close packet);
    void onCloseDoorsOfType(CloseDoorsOfType packet);
    void onCompleteTask(CompleteTask packet);
    void onEnterVent(EnterVent packet);
    void onExiled(Exiled packet);
    void onExitVent(ExitVent packet);
    void onMurderPlayer(MurderPlayer packet);
    void onPlayAnimation(PlayAnimation packet);
    void onRepairSystem(RepairSystem packet);
    void onReportDeadBody(ReportDeadBody packet);
    void onSendChat(SendChat packet);
    void onSendChatNote(SendChatNote packet);
    void onSetColor(SetColor packet);
    void onSetHat(SetHat packet);
    void onSetInfected(SetInfected packet);
    void onSetName(SetName packet);
    void onSetPet(SetPet packet);
    void onSetScanner(SetScanner packet);
    void onSetSkin(SetSkin packet);
    void onSetStartCounter(SetStartCounter packet);
    void onSetTasks(SetTasks packet);
    void onSnapTo(SnapTo packet);
    void onStartMeeting(StartMeeting packet);
    void onSyncSettings(SyncSettings packet);
    void onUpdateGameData(UpdateGameData packet);
    void onVotingComplete(VotingComplete packet);
    
    // InnerNet ???
    // void onCustomNetworkTransform(CustomNetworkTransform packet);
}