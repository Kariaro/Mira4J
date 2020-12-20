package com.sekwah.mira4j.network.inbound.packets.gamedata;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.GameDataType;
import com.sekwah.mira4j.network.inbound.packets.rpc.RPC;

public class GameDataDecoder {
    public static GameDataMessage decode(PacketBuf reader) {
        if(reader.readableBytes() == 0) return null;
        
        int length = reader.readUnsignedShort();
        GameDataType type = GameDataType.fromId(reader.readUnsignedByte());
        byte[] bytes = reader.readBytes(length);
        
        PacketBuf buf = PacketBuf.wrap(bytes);
        
        try {
            GameDataMessage msg = null;
            if(type == null) return null;
            switch(type) {
                case Data: {
                    Data data = new Data();
                    data.read(reader, false);
                    msg = data;
                    break;
                }
                case RPC: {
                    RPC rpc = new RPC();
                    rpc.read(buf);
                    msg = rpc;
                    break;
                }
                case Spawn: {
                    SpawnData data = new SpawnData();
                    data.read(buf, true);
                    msg = data;
                    break;
                }
                case Despawn: msg = new GameDataMessage.Despawn(buf.readPackedInt()); break;
                case SceneChange: msg = new GameDataMessage.SceneChange(buf.readPackedInt(), buf.readString()); break;
                case Ready: msg = new GameDataMessage.Ready(buf.readPackedInt()); break;
                case ChangeSettings: // obsolete
                    break;
            }
            
            return msg;
        } finally {
            // Mira4J.LOGGER.warn("HazelDecoder read more bytes than expected. length={} read={}", length, readBytes);
            buf.release();
        }
    }
}

