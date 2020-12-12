package com.sekwah.mira4j.network.decoder;

import com.sekwah.mira4j.Mira4J;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.GameDataType;
import com.sekwah.mira4j.network.inbound.packets.rpc.RPC;

public class GameDataDecoder {
    public static GameDataMessage decode(PacketBuf reader) {
        if(reader.readableBytes() == 0) return null;
        
        int length = reader.readUnsignedShort();
        GameDataType type = GameDataType.fromId(reader.readUnsignedByte());
        int start = reader.readerIndex();
        
        GameDataMessage msg = null;
        switch(type) {
            case Data: msg = new GameDataMessage.Data(reader.readPackedInt()); break;
            case RPC: {
                RPC rpc = new RPC();
                rpc.read(reader);
                msg = rpc;
                break;
            }
            case Spawn: // Advanced!!!
                break;
            case Despawn: msg = new GameDataMessage.Despawn(reader.readPackedInt()); break;
            case SceneChange: msg = new GameDataMessage.SceneChange(reader.readPackedInt(), reader.readString()); break;
            case Ready: msg = new GameDataMessage.Ready(reader.readPackedInt()); break;
            case ChangeSettings: // obsolete
                break;
        }
        
        int readBytes = reader.readerIndex() - start;
        
        if(readBytes < length) {
            reader.skipBytes(length - readBytes);
        } else if(readBytes > length) {
            // Invalid
            Mira4J.LOGGER.warn("HazelDecoder read more bytes than expected. length={} read={}", length, readBytes);
        }
        
        return msg;
    }
}

