package com.sekwah.mira4j.plugin.api;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sekwah.mira4j.api.Mira4JPlugin;
import com.sekwah.mira4j.plugin.api.test.TestPlugin;

public class PluginTester {
    private static List<Mira4JPlugin> plugins = new ArrayList<>();
    public static final Logger LOGGER = LogManager.getLogger("Mira4JPlugin");
    
    public static void init() {
        
    }
    
    static {
        plugins.add(new TestPlugin());
    }
    
    public static void doEvent(Event event) {
        if (event == null) return;
        Class<?> clazz = event.getClass();
        String name = clazz.getSimpleName();
        
        if (name.equals("Event") || !name.endsWith("Event")) return;
        name = name.substring(0, name.length() - 5); // Remove "<name>Event"
        
        String methodName = "on" + name;
        
        for (Mira4JPlugin plugin : plugins) {
            Method method = null;
            try {
                method = plugin.getClass().getMethod(methodName, clazz);
            } catch (Exception e) {
            }
            
            if(method == null) {
                LOGGER.warn("The plugin '{}' could not handle the event '{}'", plugin.getClass(), methodName);
                continue;
            }
            
            try {
                method.invoke(plugin, event);
            } catch (Exception e) {
                LOGGER.error("The plugin '{}' had errors inside '{}'", plugin.getClass(), methodName);
                LOGGER.throwing(e);
            }
            
        }
        
    }
}
