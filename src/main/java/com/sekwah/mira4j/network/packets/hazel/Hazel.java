package com.sekwah.mira4j.network.packets.hazel;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sekwah.mira4j.Mira4J;
import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;
import com.sekwah.mira4j.utils.NonNull;

public class Hazel {
    private static final Map<HazelType, Class<? extends HazelMessage>> map;
    static {
        Map<HazelType, Class<? extends HazelMessage>> m = new HashMap<>();
        map = Collections.unmodifiableMap(m);
        
        m.put(HazelType.HostGame, HostGame.class);
        m.put(HazelType.JoinGame, JoinGame.class);
        m.put(HazelType.StartGame, StartGame.class);
        m.put(HazelType.RemoveGame, RemoveGame.class);
        m.put(HazelType.RemovePlayer, RemovePlayer.class);
        m.put(HazelType.GameData, GameData.class);
        m.put(HazelType.GameDataTo, GameDataTo.class);
        m.put(HazelType.JoinedGame, JoinedGame.class);
        m.put(HazelType.EndGame, EndGame.class);
        // m.put(HazelType.GetGameList, GetGameList.class);
        m.put(HazelType.AlterGame, AlterGame.class);
        m.put(HazelType.KickPlayer, KickPlayer.class);
        m.put(HazelType.WaitForHost, WaitForHost.class);
        m.put(HazelType.Redirect, Redirect.class);
        // ReselectServer
        // GetGameListV2
    }
    
    private static HazelMessage newInstance(@NonNull Player player, int id) {
        HazelType type = HazelType.fromId(id);
        if (type == null) {
            Mira4J.LOGGER.warn("Unknown Hazel type id {}", id);
            return null;
        }
        
        Class<? extends HazelMessage> clazz = map.get(type);
        if (clazz == null) {
            Mira4J.LOGGER.warn("Unknown Hazel type {} does not have a class", type);
            return null;
        }
        
        try {
            return clazz.getDeclaredConstructor(Player.class).newInstance(player);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
           | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            Mira4J.LOGGER.error("Failed to create Hazel class of type {}", type);
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Used by internal classes
     * 
     * @param receiver the player that received this packet
     * @param reader a reader
     * @return a hazel message
     */
    public static HazelMessage read(Player receiver, PacketBuf reader) {
        if (reader.readableBytes() == 0) return null;
        
        reader = reader.readMessageKeepId();
        
        try {
            int typeId = reader.readUnsignedByte();
            
            HazelMessage message = newInstance(receiver, typeId);
            if (message == null) return null;
            
            message.read(reader);
            return message;
        } finally {
            reader.release();
        }
    }
    
    public static void write(PacketBuf writer, HazelMessage message) {
        writer.startMessage(message.id());
        message.write(writer);
        writer.endMessage();
    }
}