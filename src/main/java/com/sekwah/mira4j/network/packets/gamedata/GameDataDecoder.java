package com.sekwah.mira4j.network.packets.gamedata;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.GameDataType;
import com.sekwah.mira4j.network.packets.rpc.RPC;
import com.sekwah.mira4j.unity.Scene;

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
                    Data data = new Data(scene);
                    data.read(reader, isSpawning);
                    msg = data;
                    break;
                }
                case RPC: {
                    RPC rpc = new RPC();
                    rpc.read(reader);
                    msg = rpc;
                    break;
                }
                case Spawn: {
                    SpawnData data = new SpawnData();
                    data.read(reader, isSpawning);
                    msg = data;
                    break;
                }
                case Despawn: msg = new GameDataMessage.Despawn(reader.readPackedInt()); break;
                case SceneChange: msg = new GameDataMessage.SceneChange(reader.readPackedInt(), reader.readString()); break;
                case Ready: msg = new GameDataMessage.Ready(reader.readPackedInt()); break;
                case ChangeSettings: // obsolete
                    break;
            }
            
            return msg;
        } finally {
            reader.release();
        }
    }
}

