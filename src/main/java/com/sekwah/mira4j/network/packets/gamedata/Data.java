package com.sekwah.mira4j.network.packets.gamedata;

import java.util.*;

import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.GameDataType;
import com.sekwah.mira4j.network.packets.net.Component;
import com.sekwah.mira4j.network.packets.net.InnerNet;

public class Data implements GameDataMessage {
    private final Scene scene;
    
    private Component[] components;
    
    public Data(Scene scene) {
        this.scene = scene;
    }
    
    @Override
    public void read(PacketBuf reader, boolean isSpawning) {
        if(scene == null) {
            components = new Component[0];
            return;
        }
        
        List<Component> list = new ArrayList<>();
        
        Component comp;
        while ((comp = InnerNet.read(reader, scene)) != null) {
            list.add(comp);
        }
        
        components = list.toArray(new Component[0]);
    }
    
    @Override
    public void write(PacketBuf writer, boolean isSpawning) {
        
    }
    
    @Override
    public int id() {
        return GameDataType.Data.getId();
    }
    
    public Component[] getComponents() {
        return components;
    }
    
    public String toString() {
        return String.format("Data { components=%s }",
            Arrays.deepToString(components)
        );
    }
}
