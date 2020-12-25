package com.sekwah.mira4j;

import java.io.File;
import java.net.BindException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sekwah.mira4j.config.ServerConfig;
import com.sekwah.mira4j.data.DataStorage;
import com.sekwah.mira4j.impl.unity.GameManager;
import com.sekwah.mira4j.network.Server;

public class Mira4J {

    private final DataStorage dataStorage;
    private final Server server;

    private final String SERVER_SETTINGS_LOC = "serversettings.json";

    public static final Logger LOGGER = LogManager.getLogger("Mira4J");

    public Mira4J() {
        dataStorage = new DataStorage(new File("./"));

        ServerConfig serverConfig = dataStorage.loadJson(ServerConfig.class, SERVER_SETTINGS_LOC);

        dataStorage.storeJson(serverConfig, SERVER_SETTINGS_LOC);
        
        // Initialize the game manager
        GameManager.init();
        
        server = new Server(serverConfig);
        
        try {
            server.start();
        } catch (BindException e) {
            LOGGER.error("Port already in use");
        } catch (Exception e) {
            LOGGER.error("Server crashed");
            e.printStackTrace();
            System.exit(-1);
        } finally {
            System.exit(0);
        }
    }
}
