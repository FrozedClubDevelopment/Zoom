package me.ryzeon.core;

import me.ryzeon.core.manager.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ZoomAPI {
    // View last server
    public String getLastServer(Player player) {
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        if (playerData != null) {
            return playerData.getLastserver();
        }
        return null;
    }

    public boolean isStaffChat(Player player) {
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        if (playerData != null) {
            return playerData.isStaffchat();
        }
        return false;
    }

    public boolean isAdminChat(Player player) {
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        if (playerData != null) {
            return playerData.isAdminchat();
        }
        return false;
    }

    public ChatColor getNameColor(Player player) {
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        if (playerData != null) {
            return ChatColor.valueOf(playerData.getNamecolor());
        }
        return null;
    }

    public ChatColor getChatColor(Player player) {
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        if (playerData != null) {
            return ChatColor.valueOf(playerData.getChatColor());
        }
        return null;
    }
}
