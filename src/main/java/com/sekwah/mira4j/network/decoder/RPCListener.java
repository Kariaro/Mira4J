package com.sekwah.mira4j.network.decoder;

import com.sekwah.mira4j.network.packets.rpc.*;

public interface RPCListener {
    void onAddVote(RPC rpc, AddVote packet);
    void onCastVote(RPC rpc, CastVote packet);
    void onCheckColor(RPC rpc, CheckColor packet);
    void onCheckName(RPC rpc, CheckName packet);
    void onClearVote(RPC rpc, ClearVote packet);
    void onClose(RPC rpc, Close packet);
    void onCloseDoorsOfType(RPC rpc, CloseDoorsOfType packet);
    void onCompleteTask(RPC rpc, CompleteTask packet);
    void onEnterVent(RPC rpc, EnterVent packet);
    void onExiled(RPC rpc, Exiled packet);
    void onExitVent(RPC rpc, ExitVent packet);
    void onMurderPlayer(RPC rpc, MurderPlayer packet);
    void onPlayAnimation(RPC rpc, PlayAnimation packet);
    void onRepairSystem(RPC rpc, RepairSystem packet);
    void onReportDeadBody(RPC rpc, ReportDeadBody packet);
    void onSendChat(RPC rpc, SendChat packet);
    void onSendChatNote(RPC rpc, SendChatNote packet);
    void onSetColor(RPC rpc, SetColor packet);
    void onSetHat(RPC rpc, SetHat packet);
    void onSetInfected(RPC rpc, SetInfected packet);
    void onSetName(RPC rpc, SetName packet);
    void onSetPet(RPC rpc, SetPet packet);
    void onSetScanner(RPC rpc, SetScanner packet);
    void onSetSkin(RPC rpc, SetSkin packet);
    void onSetStartCounter(RPC rpc, SetStartCounter packet);
    void onSetTasks(RPC rpc, SetTasks packet);
    void onSnapTo(RPC rpc, SnapTo packet);
    void onStartMeeting(RPC rpc, StartMeeting packet);
    void onSyncSettings(RPC rpc, SyncSettings packet);
    void onUpdateGameData(RPC rpc, UpdateGameData packet);
    void onVotingComplete(RPC rpc, VotingComplete packet);
}
