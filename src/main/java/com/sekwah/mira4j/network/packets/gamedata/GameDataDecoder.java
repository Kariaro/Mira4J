package com.sekwah.mira4j.network.packets.gamedata;

import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.GameDataType;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage.Despawn;
import com.sekwah.mira4j.network.packets.gamedata.GameDataMessage.Ready;
import com.sekwah.mira4j.network.packets.rpc.RPC;

public class GameDataDecoder {
    public static GameDataMessage decode(Scene scene, PacketBuf reader, boolean isSpawning) {
        if (reader.readableBytes() == 0) return null;
        if (scene == null) return null;
        
        reader = reader.readMessageKeepId();
        GameDataType type = GameDataType.fromId(reader.readUnsignedByte());
        if (type == null) return null;
        
        try {
            switch(type) {
                case Data: {
                    DataMessage pck = new DataMessage(scene);
                    pck.read(reader, false);
                    return pck;
                }
                case RPC: {
                    RPC pck = new RPC();
                    pck.read(reader, isSpawning);
                    return pck;
                }
                case Spawn: {
                    SpawnMessage pck = new SpawnMessage(scene);
                    pck.read(reader, true);
                    return pck;
                }
                case Despawn: {
                    Despawn pck = new Despawn();
                    pck.read(reader, isSpawning);
                    return pck;
                }
                case Ready: {
                    Ready pck = new Ready();
                    pck.read(reader, isSpawning);
                    return pck;
                }
                case SceneChange: return new GameDataMessage.SceneChange(reader.readPackedInt(), reader.readString());
                case ChangeSettings: // obsolete
                    break;
            }
            
            return null;
        } finally {
            reader.release();
        }
    }
}

