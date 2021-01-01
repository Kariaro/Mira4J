package com.sekwah.mira4j.network.packets.gamedata;

import java.util.*;

import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.network.PacketBuf;
import com.sekwah.mira4j.network.Packets.GameDataType;
import com.sekwah.mira4j.network.decoder.ClientInListener;
import com.sekwah.mira4j.network.packets.net.Component;
import com.sekwah.mira4j.network.packets.net.InnerNet;

public class DataMessage implements GameDataMessage {
    private final Scene scene;
    private Component[] components;
    
    public DataMessage(Scene scene) {
        this.scene = scene;
    }
    
    public DataMessage(Scene scene, Component... componenents) {
        this.scene = scene;
        this.components = componenents;
    }
    
    @Override
    public void read(PacketBuf reader, boolean isSpawning) {
        if (scene == null) {
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
        for (Component comp : components) {
            comp.write(writer, isSpawning);
        }
    }
    
    @Override
    public int id() {
        return GameDataType.Data.getId();
    }
    
    @Override
    public void forwardPacket(ClientInListener listener) {
        for (Component c : components) {
            c.forwardPacket(listener);
        }
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
