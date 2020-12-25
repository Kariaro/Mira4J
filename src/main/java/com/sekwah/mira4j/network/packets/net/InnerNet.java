package com.sekwah.mira4j.network.packets.net;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sekwah.mira4j.Mira4J;
import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.NetType;

public class InnerNet {
    private static final Map<NetType, Class<? extends Component>> map;
    static {
        Map<NetType, Class<? extends Component>> m = new HashMap<>();
        map = Collections.unmodifiableMap(m);
        
        m.put(NetType.LobbyBehaviour, LobbyBehaviour.class);
        m.put(NetType.PlayerControl, PlayerControl.class);
        m.put(NetType.PlayerPhysics, PlayerPhysics.class);
        m.put(NetType.CustomNetworkTransform, CustomNetworkTransform.class);
        m.put(NetType.NetGameData, NetGameData.class);
    }
    
    private static Component newInstance(int id) {
        NetType type = NetType.fromId(id);
        if(type == null) {
            Mira4J.LOGGER.warn("Unknown InnerNet type id {}", id);
            return null;
        }
        
        Class<? extends Component> clazz = map.get(type);
        if(clazz == null) {
            Mira4J.LOGGER.warn("Unknown InnerNet type {} does not have a class", type);
            return null;
        }
        
        try {
            return clazz.getConstructor().newInstance();
        } catch(InstantiationException | IllegalAccessException | IllegalArgumentException
           | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            Mira4J.LOGGER.error("Failed to create InnerNet class of type {}", type);
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static Component read(PacketBuf reader, int spawnType, boolean isSpawning) {
        Component message = newInstance(spawnType);
        if(message == null) return null;
        message.read(reader, isSpawning);
        return message;
    }
    
    public static Component read(PacketBuf reader, Scene scene) {
        if(scene == null) return null;
        if(reader.readableBytes() < 1) return null;
        
        reader.markReaderIndex();
        int netId = reader.readUnsignedPackedInt();
        reader.resetReaderIndex();
        
        Component message = scene.getComponent(netId);
        if(message == null) return null;
        
        message.read(reader, false);
        
        return message;
    }
}
