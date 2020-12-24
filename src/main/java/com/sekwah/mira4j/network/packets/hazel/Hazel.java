package com.sekwah.mira4j.network.packets.hazel;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sekwah.mira4j.Mira4J;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.HazelType;

@SuppressWarnings("deprecation")
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
        // GameDataTo
        m.put(HazelType.JoinedGame, JoinedGame.class);
        m.put(HazelType.EndGame, EndGame.class);
        m.put(HazelType.GetGameList, GetGameList.class);
        m.put(HazelType.AlterGame, AlterGame.class);
        m.put(HazelType.KickPlayer, KickPlayer.class);
        m.put(HazelType.WaitForHost, WaitForHost.class);
        // Redirect
        // ReselectServer
        // GetGameListV2
    }
    
    private static HazelMessage newInstance(int id) {
        HazelType type = HazelType.fromId(id);
        if(type == null) {
            Mira4J.LOGGER.warn("Unknown Hazel type id {}", id);
            return null;
        }
        
        Class<? extends HazelMessage> clazz = map.get(type);
        if(clazz == null) {
            Mira4J.LOGGER.warn("Unknown Hazel type {} does not have a class", type);
            return null;
        }
        
        try {
            return clazz.getConstructor().newInstance();
        } catch(InstantiationException | IllegalAccessException | IllegalArgumentException
           | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            Mira4J.LOGGER.error("Failed to create Hazel class of type {}", type);
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static HazelMessage read(PacketBuf reader) {
        if(reader.readableBytes() == 0) return null;
        
        int length = reader.readUnsignedShort();
        int typeId = reader.readUnsignedByte();
        byte[] bytes = reader.readBytes(length); // Always read the correct amount of bytes
        
        HazelMessage message = newInstance(typeId);
        if(message == null) return null;
        
        PacketBuf buf = PacketBuf.wrap(bytes);
        message.read(buf);
        buf.release();
        
        return message;
    }
    
    public static void write(PacketBuf writer, HazelMessage message) {
        PacketBuf buf = PacketBuf.create(4096);
        message.write(buf);
        byte[] bytes = buf.readBytes(buf.readableBytes());
        buf.release();
        
        writer.writeShort(bytes.length);
        writer.writeUnsignedByte(message.id());
        writer.writeBytes(bytes);
    }
}