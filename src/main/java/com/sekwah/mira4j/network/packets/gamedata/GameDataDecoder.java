package com.sekwah.mira4j.network.packets.gamedata;

import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.GameDataType;
import com.sekwah.mira4j.network.packets.rpc.RPC;

public class GameDataDecoder {
    public static GameDataMessage decode(Scene scene, PacketBuf reader, boolean isSpawning) {
        if (reader.readableBytes() == 0) return null;
        
        reader = reader.readMessageKeepId();
        GameDataType type = GameDataType.fromId(reader.readUnsignedByte());
        
        try {
            GameDataMessage msg = null;
            if (type == null) return null;
            switch(type) {
                case Data: {
                    if(scene == null) return null;
                    
                    Data data = new Data(scene);
                    data.read(reader, isSpawning);
                    return data;
                }
                case RPC: {
                    if(scene == null) return null;
                    
                    RPC rpc = new RPC();
                    rpc.read(reader);
                    return rpc;
                }
                case Spawn: {
                    SpawnData data = new SpawnData();
                    data.read(reader, isSpawning);
                    return data;
                }
                case Despawn: return new GameDataMessage.Despawn(reader.readPackedInt());
                case SceneChange: return new GameDataMessage.SceneChange(reader.readPackedInt(), reader.readString());
                case Ready: return new GameDataMessage.Ready(reader.readPackedInt());
                case ChangeSettings: // obsolete
                    break;
            }
            
            return msg;
        } finally {
            reader.release();
        }
    }
}

