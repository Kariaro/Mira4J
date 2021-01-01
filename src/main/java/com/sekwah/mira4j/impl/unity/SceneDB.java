package com.sekwah.mira4j.impl.unity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.sekwah.mira4j.Mira4J;
import com.sekwah.mira4j.api.Player;
import com.sekwah.mira4j.api.Scene;
import com.sekwah.mira4j.network.packets.net.Component;
import com.sekwah.mira4j.network.packets.net.ComponentDB;
import com.sekwah.mira4j.utils.GameUtils;

public class SceneDB implements Scene {
    private final Map<Integer, Component> objects = new HashMap<>();
    private final Player[] players = new Player[16];
    private final int gameId;

    public SceneDB(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public int getGameId() {
        return gameId;
    }

    @Override
    public Player getPlayerFromClientId(int clientId) {
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            if (player != null && clientId == player.getClientId()) {
                return player;
            }
        }
        
        return null;
    }

    @Override
    public List<Player> getPlayers() {
        List<Player> list = new ArrayList<>();
        
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            if (player != null) {
                list.add(player);
            }
        }
        
        return Collections.unmodifiableList(list);
    }

    @Override
    public Player getPlayer(int playerId) {
        if (playerId < 0 || playerId >= players.length) return null;
        return players[playerId];
    }

    @Override
    public int getNumPlayers() {
        int length = 0;
        
        for (int i = 0; i < players.length; i++) {
            length += (players[i] == null) ? 0:1;
        }
        
        return length;
    }

    @Override
    public void addPlayer(Player player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = player;
                ((PlayerDB)player).playerId = i;
                break;
            }
        }
    }

    @Override
    public void removePlayer(Player player) {
        int clientId = player.getClientId();
        
        for (int i = 0; i < players.length; i++) {
            Player plr = players[i];
            if (plr != null && clientId == plr.getClientId()) {
                players[i] = null;
                break;
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Scene{ game=\"%s\" }", GameUtils.getStringFromGameId(gameId));
    }

    @Override
    public void addComponent(Player player, Component component) {
        ComponentDB cdb = (ComponentDB)component;
        PlayerDB pdb = (PlayerDB)player;
        
        if(cdb.getPlayer() == null && cdb.getScene() == null) {
            cdb.load(this, player);
            System.out.println(cdb + ", Adding scene to component!");
            if(pdb != null) {
                pdb.addComponent(component);
            }
            objects.put(component.getNetId(), component);
        } else {
            throw new IllegalStateException("Component already connected to a player");
        }
    }

    @Override
    public void removeComponent(Player player, Component component) {
        objects.remove(component.getNetId());
        // ???
    }

    @Override
    public Component getComponent(int netId) {
        return objects.get(netId);
    }

    @Override
    public <T extends Component> T getComponent(Class<T> type, int netId) {
        if (type == null) throw new IllegalArgumentException();
        
        Component object = objects.get(netId);
        if (type.isInstance(object)) {
            return type.cast(object);
        }
        
        return null;
    }

    @Override
    @Deprecated
    public void tick() {
        // TODO: Should we tick stuff here?
    }
    
    private AtomicInteger atom = new AtomicInteger(128);
    public <T extends Component> T spawnComponent(Class<T> clazz, Player player) {
        Constructor<T> constructor;
        
        try {
            constructor = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException | SecurityException e) {
            Mira4J.LOGGER.throwing(e);
            e.printStackTrace();
            return null;
        }
        
        T result;
        try {
            result = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            Mira4J.LOGGER.throwing(e);
            e.printStackTrace();
            return null;
        }
        
        // TODO: Get next available index
        ComponentDB db = (ComponentDB)result;
        db.setNetId(atom.getAndIncrement());
        
        addComponent(player, result);
        return result;
    }
}
