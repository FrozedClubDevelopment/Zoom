package club.frozed.core;

import club.frozed.core.manager.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Zoom {
    // View last server
    public String getLastServer(Player player) {
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        if (playerData != null) {
            return playerData.getLastServer();
        }
        return null;
    }

    public boolean isStaffChat(Player player) {
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        if (playerData != null) {
            return playerData.isStaffChat();
        }
        return false;
    }

    public boolean isAdminChat(Player player) {
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        if (playerData != null) {
            return playerData.isAdminChat();
        }
        return false;
    }

    public ChatColor getNameColor(Player player) {
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        if (playerData != null) {
            return ChatColor.valueOf(playerData.getNameColor());
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

    public boolean isSocialSpy(Player player) {
        PlayerData data = PlayerData.getByUuid(player.getUniqueId());
        if (data != null) {
            return data.isSocialSpy();
        }
        return false;
    }
}
