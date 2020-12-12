package com.sekwah.mira4j.network.decoder;

import com.sekwah.mira4j.Mira4J;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets;
import com.sekwah.mira4j.network.Packets.MessageType;

public class HazelDecoder {
    public static HazelMessage decode(PacketBuf reader) {
        if(reader.readableBytes() == 0) return null;
        
        int length = reader.readUnsignedShort();
        int type = reader.readUnsignedByte();
        
        HazelMessage packet = Packets.getHazelPacket(MessageType.fromId(type));
        if(packet == null) {
            reader.skipBytes(length);
            return null;
        }
        
        int start = reader.readerIndex();
        packet.readData(reader);
        int readBytes = reader.readerIndex() - start;
        
        if(readBytes < length) {
            reader.skipBytes(length - readBytes);
        } else if(readBytes > length) {
            // Invalid
            Mira4J.LOGGER.warn("HazelDecoder read more bytes than expected. length={} read={}", length, readBytes);
        }
        
        return packet;
    }
    
    public static Object decode(PacketBuf reader, java.util.function.Function<PacketBuf, Object> func) {
        int remainingBytes = reader.readableBytes();
        if(remainingBytes == 0) return null;
        
        // TODO: Check that we have enough bytes or generate an error.
        int length = reader.readUnsignedShort();
        int type = reader.readUnsignedByte();
        
        
        return null;
    }
}

