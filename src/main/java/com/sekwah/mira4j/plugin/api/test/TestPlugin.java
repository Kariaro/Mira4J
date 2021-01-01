package com.sekwah.mira4j.plugin.api.test;

import com.sekwah.mira4j.api.Mira4JPlugin;
import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.config.Vector2;
import com.sekwah.mira4j.plugin.api.ChatEvent;
import com.sekwah.mira4j.plugin.api.MoveEvent;

public class TestPlugin extends Mira4JPlugin {
    @Override
    public void onEnable() {
        
    }
    
    @Override
    public void onDisable() {
        
    }
    
    public void onChat(ChatEvent event) {
        Player player = event.getPlayer();
        System.out.println(event);
        
        String msg = event.getMessage();
        if(msg.startsWith("/n ")) {
            String test = msg.substring(3).trim();
            
            if(!test.isEmpty()) {
                player.setName(test);
            }
        }
        
    }
    
    public void onMove(MoveEvent event) {
        // System.out.println(event);
        
        Player player = event.getPlayer();
        
//        String str = String.format("loc=%s vel=%s", event.getLocation(), event.getVelocity());
//        player.setName(str);
//        
//        Vector2 vel = event.getVelocity();
//        
//        if(Math.abs(vel.x) < 0.1) vel.x = 0;
//        if(Math.abs(vel.y) < 0.1) vel.y = 0;
        
//        player.setLocation(event.getLocation().add(vel.normalize().mul(0.4f)));
//        player.setColorId((int)(Math.random() * 12));
//        player.setHatId(18 + (int)(Math.random() * 95));
//        player.setSkinId((int)(Math.random() * 95));
//        player.setPetId((int)(Math.random() * 9));
    }
}
