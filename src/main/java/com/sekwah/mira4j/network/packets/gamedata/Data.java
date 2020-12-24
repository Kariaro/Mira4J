package com.sekwah.mira4j.network.packets.gamedata;

import java.util.Arrays;

import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.GameDataType;
import com.sekwah.mira4j.network.packets.net.Component;

public class Data implements GameDataMessage {
    private int netId;
    private Component[] components;
    
    public Data() {
        
    }
    
    @Override
    public void read(PacketBuf reader, boolean isSpawning) {
        // Problem here is that we need to access the component with the netId provided.
//        {
//            reader.markReaderIndex();
//            byte[] bytes = reader.readBytes(reader.readableBytes());
//            System.out.println("Array: " + Arrays.toString(bytes));
//            reader.resetReaderIndex();
//        }
//        
//        netId = reader.readUnsignedPackedInt();
//        
//        int length = reader.readUnsignedPackedInt();
//        components = new Component[length];
//        
//        for(int i = 0; i < length; i++) {
//            Component object = InnerNet.read(reader, netId, isSpawning);
//            components[i] = object;
//        }
    }
    
    @Override
    public void write(PacketBuf writer, boolean isSpawning) {
        
    }
    
    @Override
    public int id() {
        return GameDataType.Data.getId();
    }
    
    public int getNetId() {
        return netId;
    }
    
    public Component[] getComponents() {
        return components;
    }
    
    public String toString() {
        return String.format("Data { netId=%d, components=%s }",
            netId,
            Arrays.deepToString(components)
        );
    }
}
