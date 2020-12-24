package com.sekwah.mira4j.network;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.sekwah.mira4j.Mira4J;
import com.sekwah.mira4j.network.packets.*;

public class Packets {
    private static final Map<PacketType, Class<? extends Packet<?>>> packets;
    
    static {
        packets = new HashMap<>();
        packets.put(PacketType.NORMAL, NormalPacket.class);
        packets.put(PacketType.RELIABLE, ReliablePacket.class);
        packets.put(PacketType.HELLO, HelloPacket.class);
        packets.put(PacketType.DISCONNECT, DisconnectPacket.class);
        packets.put(PacketType.ACKNOWLEDGEMENT, AcknowledgePacket.class);
        packets.put(PacketType.PING, PingPacket.class);
    }
    
    public static Packet<?> getPacketFromType(PacketType type) {
        Class<? extends Packet<?>> clazz = packets.get(type);
        if(clazz == null) {
            Mira4J.LOGGER.error("Failed to create packet of type {}", type.toString());
            return null;
        }
        
        try {
            return clazz.getConstructor().newInstance();
        } catch(InstantiationException | IllegalAccessException | IllegalArgumentException
           | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            Mira4J.LOGGER.error("Failed to create packet of type {}", type.toString());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static PacketType getPacketType(Class<?> clazz) {
        for(PacketType key : packets.keySet()) {
            Class<? extends Packet<?>> item = packets.get(key);
            if(item.equals(clazz)) return key;
        }
        return null;
    }
    
    public enum PacketType {
        NORMAL(0x00),
        RELIABLE(0x01),
        HELLO(0x08),
        DISCONNECT(0x09),
        ACKNOWLEDGEMENT(0x0a),
        FRAGMENT(0x0b),
        PING(0x0c);

        private int id;

        PacketType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static PacketType fromId(int id) {
            for (PacketType type : values()) {
                if (type.getId() == id) {
                    return type;
                }
            }
            return null;
        }
    }

    public enum Maps {
        THE_SKELD(0),
        MIRA_HQ(1),
        POLUS(2);

        private int id;

        Maps(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Maps fromId(int id) {
            for (Maps type : values()) {
                if (type.getId() == id) {
                    return type;
                }
            }
            return null;
        }
    }
    
    public enum HazelType {
        HostGame(0),
        JoinGame(1),
        StartGame(2),
        RemoveGame(3),
        RemovePlayer(4),
        GameData(5),
        GameDataTo(6),
        JoinedGame(7),
        EndGame(8),
        GetGameList(9),
        AlterGame(10),
        KickPlayer(11),
        WaitForHost(12),
        Redirect(13),
        ReselectServer(14),
        GetGameListV2(16)
        ;
        
        final int id;
        HazelType(int id) {
            this.id = id;
        }
        
        public int getId() {
            return id;
        }
        
        public static HazelType fromId(int id) {
            for (HazelType type : values()) {
                if (type.id == id) {
                    return type;
                }
            }
            return null;
        }
    }
    
    public enum GameDataType {
        Data(1),
        RPC(2),
        // ???
        Spawn(4),
        Despawn(5),
        SceneChange(6),
        Ready(7),
        ChangeSettings(8),
        ;
        
        final int id;
        GameDataType(int id) {
            this.id = id;
        }
        
        public int getId() {
            return id;
        }
        
        public static GameDataType fromId(int id) {
            for (GameDataType type : values()) {
                if (type.id == id) {
                    return type;
                }
            }
            return null;
        }
    }
    
    public enum RPCType {
        PlayAnimation(0x00),
        CompleteTask(0x01),
        SyncSettings(0x02),
        SetInfected(0x03),
        Exiled(0x04),
        CheckName(0x05),
        SetName(0x06),
        CheckColor(0x07),
        SetColor(0x08),
        SetHat(0x09),
        SetSkin(0x0a),
        ReportDeadBody(0x0b),
        MurderPlayer(0x0c),
        SendChat(0x0d),
        StartMeeting(0x0e),
        SetScanner(0x0f),
        SendChatNote(0x10),
        SetPet(0x11),
        SetStartCounter(0x12),
        EnterVent(0x13),
        ExitVent(0x14),
        SnapTo(0x15),
        Close(0x16),
        VotingComplete(0x17),
        CastVote(0x18),
        ClearVote(0x19),
        AddVote(0x1a),
        CloseDoorsOfType(0x1b),
        RepairSystem(0x1c),
        SetTasks(0x1d),
        UpdateGameData(0x1e)
        ;
        
        final int id;
        private RPCType(int id) {
            this.id = id;
        }
        
        public int getId() {
            return id;
        }
        
        public static RPCType fromId(int id) {
            for (RPCType type : values()) {
                if (type.id == id) {
                    return type;
                }
            }
            return null;
        }
    }
    
    // Testing
    public enum SpawnType {
        SHIP_STATUS(0),
        MEETING_HUD(1),
        LOBBY_BEHAVIOUR(2),
        GAME_DATA(3),
        PLAYER_CONTROL(4),
        HEADQUARTERS(5),
        PLANET_MAP(6),
        APRIL_SHIP_STATUS(7),
        ;
        
        final int id;
        private SpawnType(int id) {
            this.id = id;
        }
        
        public int getId() {
            return id;
        }
        
        public static SpawnType fromId(int id) {
            for (SpawnType type : values()) {
                if (type.id == id) {
                    return type;
                }
            }
            return null;
        }
    }
    
    public enum NetType {
        ShipStatus(0),
        MeetingHud(1),
        LobbyBehaviour(2),
        GameData(3),
        PlayerControl(4),
        Headquarters(5),
        PlanetMap(6),
        AprilShipStatus(7),
        VoteBanSystem(8),
        PlayerPhysics(9),
        CustomNetworkTransform(10),
        ;
        
        final int id;
        private NetType(int id) {
            this.id = id;
        }
        
        public int getId() {
            return id;
        }
        
        public static NetType fromId(int id) {
            for (NetType type : values()) {
                if (type.id == id) {
                    return type;
                }
            }
            return null;
        }
    }
}
