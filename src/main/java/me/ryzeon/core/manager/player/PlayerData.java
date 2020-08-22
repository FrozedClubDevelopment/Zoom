package me.ryzeon.core.manager.player;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class PlayerData {

    List<String> players = new ArrayList<>();

    List<UUID> playersuuid = new ArrayList<>();

    private String name;

    private UUID uuid;

    PlayerData(String name,UUID uuid){
        this.name = name;
        this.uuid = uuid;
        players.add(name);
        playersuuid.add(uuid);
    }
}
