package com.sekwah.mira4j.network.inbound.packets.rpc;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sekwah.mira4j.Mira4J;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.RPCType;
import com.sekwah.mira4j.network.decoder.BufferObject;
import com.sekwah.mira4j.network.decoder.GameDataMessage;

public class RPC implements BufferObject, GameDataMessage {
    private static final Map<RPCType, Class<? extends RPCObject>> map;
    static {
        Map<RPCType, Class<? extends RPCObject>> m = new HashMap<>();
        map = Collections.unmodifiableMap(m);
        
        m.put(RPCType.PlayAnimation, PlayAnimation.class);
        m.put(RPCType.CompleteTask, CompleteTask.class);
        m.put(RPCType.SyncSettings, SyncSettings.class);
        m.put(RPCType.SetInfected, SetInfected.class);
        m.put(RPCType.Exiled, Exiled.class);
        m.put(RPCType.CheckName, CheckName.class);
        m.put(RPCType.SetName, SetName.class);
        m.put(RPCType.CheckColor, CheckColor.class);
        m.put(RPCType.SetColor, SetColor.class);
        m.put(RPCType.SetHat, SetHat.class); // 10/30
        
        m.put(RPCType.SetSkin, SetSkin.class);
        m.put(RPCType.ReportDeadBody, ReportDeadBody.class);
        m.put(RPCType.MurderPlayer, MurderPlayer.class);
        m.put(RPCType.SendChat, SendChat.class);
        m.put(RPCType.StartMeeting, StartMeeting.class);
        m.put(RPCType.SetScanner, SetScanner.class);
        m.put(RPCType.SendChatNote, SendChatNote.class);
        m.put(RPCType.SetPet, SetPet.class);
        m.put(RPCType.SetStartCounter, SetStartCounter.class);
        m.put(RPCType.EnterVent, EnterVent.class); // 20/30
        
        m.put(RPCType.ExitVent, ExitVent.class);
        m.put(RPCType.SnapTo, SnapTo.class);
        m.put(RPCType.Close, Close.class);
        m.put(RPCType.VotingComplete, VotingComplete.class);
        m.put(RPCType.CastVote, CastVote.class);
        m.put(RPCType.ClearVote, ClearVote.class);
        m.put(RPCType.AddVote, AddVote.class);
        m.put(RPCType.CloseDoorsOfType, CloseDoorsOfType.class);
        m.put(RPCType.RepairSystem, RepairSystem.class);
        m.put(RPCType.UpdateGameData, UpdateGameData.class); // 30/30
    }
    
    private static RPCObject newInstance(int id) {
        RPCType type = RPCType.fromId(id);
        if(type == null) {
            Mira4J.LOGGER.warn("Unknown RPC type id {}", id);
            return null;
        }
        
        Class<? extends RPCObject> clazz = map.get(type);
        if(clazz == null) {
            Mira4J.LOGGER.warn("Unknown RPC type {} does not have a class", type);
            return null;
        }
        
        try {
            return clazz.getConstructor().newInstance();
        } catch(InstantiationException | IllegalAccessException | IllegalArgumentException
           | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            Mira4J.LOGGER.error("Failed to create RPC class of type {}", type);
            e.printStackTrace();
        }
        
        return null;
    }
    
    private int senderNetId;
    private RPCObject message;
    
    public RPC() {
        
    }
    
    public RPC(int senderNetId, RPCObject message) {
        this.senderNetId = senderNetId;
        this.message = message;
    }
    
    public void read(PacketBuf reader) {
        senderNetId = reader.readUnsignedPackedInt();
        
        int callId = reader.readUnsignedByte();
        message = newInstance(callId);
        if(message == null) return;
        
        message.read(reader);
    }
    
    public void write(PacketBuf writer) {
        writer.writeUnsignedPackedInt(senderNetId);
        writer.writeUnsignedByte(message.id());
        message.write(writer);;
    }
    
    public int getSenderNetId() {
        return senderNetId;
    }
    
    public RPCObject getMessage() {
        return message;
    }
    
    @Override
    public String toString() {
        return String.format("RPC[ senderNetId=%d, message=%s ]", senderNetId, message);
    }
}
