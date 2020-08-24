package me.ryzeon.core;

import me.ryzeon.core.manager.player.PlayerData;
import org.bukkit.entity.Player;

public class ZoomAPI {
    // View last server
    public String getLastServer(Player player) {
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        return playerData.getLastserver();
    }

    public boolean isStaffChat(Player player) {
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        return playerData.isStaffchat();
    }

    public boolean isAdminChat(Player player) {
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        return playerData.isAdminchat();
    }
}
