package com.sekwah.mira4j.game;

import com.sekwah.mira4j.network.Packets.Maps;

// Use gson maybe?
public class GameOptionsData {
    public int version;
    public int maxPlayers = 10;
    public long keywords = 1;
    public Maps maps = Maps.MIRA_HQ;
    public float playerSpeedModifier = 1.0f;
    public float crewmateLightModifier = 1.0f;
    public float impostorLightModifier = 1.5f;
    public float killCooldown = 45;
    public int numCommonTasks = 1;
    public int numLongTasks = 1;
    public int numShortTasks = 2;
    public long numEmergencyMeetings = 1;
    public int numImpostors = 1;
    public int killDistance = 1;
    public long discussionTime = 15;
    public long votingTime = 120;
    public boolean isDefaults = true;
    public int emergencyCooldown = 15;
    public boolean confirmEjects = true;
    public boolean visualTasks = true;
    public boolean anonymousVotes = false;
    public int taskBarUpdates = 0;
    
    
    public void load(GameOptionsData data) {
        version = data.version;
        maxPlayers = data.maxPlayers;
        keywords = data.keywords;
        maps = data.maps;
        playerSpeedModifier = data.playerSpeedModifier;
        crewmateLightModifier = data.crewmateLightModifier;
        impostorLightModifier = data.impostorLightModifier;
        killCooldown = data.killCooldown;
        numCommonTasks = data.numCommonTasks;
        numLongTasks = data.numLongTasks;
        numShortTasks = data.numShortTasks;
        numEmergencyMeetings = data.numEmergencyMeetings;
        numImpostors = data.numImpostors;
        killDistance = data.killDistance;
        discussionTime = data.discussionTime;
        votingTime = data.votingTime;
        isDefaults = data.isDefaults;
        emergencyCooldown = data.emergencyCooldown;
        confirmEjects = data.confirmEjects;
        visualTasks = data.visualTasks;
        anonymousVotes = data.anonymousVotes;
        taskBarUpdates = data.taskBarUpdates;
    }
}
