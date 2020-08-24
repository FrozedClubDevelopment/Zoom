package me.ryzeon.core.listeners;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.manager.chat.ChatManager;
import me.ryzeon.core.manager.player.PlayerData;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.config.ConfigCursor;
import me.ryzeon.core.utils.lang.Lang;
import me.ryzeon.core.utils.time.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onStaffChat(AsyncPlayerChatEvent e) {
        PlayerData playerData = PlayerData.getByName(e.getPlayer().getName());
        ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getSettingsconfig(), "chat.staff-chat");
        boolean staffchat = playerData.isStaffchat();
        boolean adminchat = playerData.isAdminchat();
        if (staffchat && !adminchat) {
            e.setCancelled(true);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("core.staffchat")) {
                    player.sendMessage(Color.translate(configCursor.getString("format")
                            .replace("<server>", Lang.SERVER_NAME)
                            .replace("<player>", playerData.getPlayer().getName())
                            .replace("<text>", e.getMessage())));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onAdminChat(AsyncPlayerChatEvent e) {
        PlayerData playerData = PlayerData.getByUuid(e.getPlayer().getUniqueId());
        ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getSettingsconfig(), "chat.admin-chat");
        boolean staffchat = playerData.isStaffchat();
        boolean adminchat = playerData.isAdminchat();
        if (adminchat && !staffchat || adminchat && staffchat) {
            e.setCancelled(true);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("core.adminchat")) {
                    player.sendMessage(Color.translate(configCursor.getString("format")
                            .replace("<server>", Lang.SERVER_NAME)
                            .replace("<player>", playerData.getPlayer().getName())
                            .replace("<text>", e.getMessage())));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void chatDelay(AsyncPlayerChatEvent e) {
        PlayerData playerData = PlayerData.getByUuid(e.getPlayer().getUniqueId());
        ChatManager chatManager = Zoom.getInstance().getChatManager();
        ConfigCursor msg = new ConfigCursor(Zoom.getInstance().getMessagesconfig(), "chat.playermsg");
        Cooldown cooldown = new Cooldown(chatManager.getDelay());
        if (chatManager.isMute()) {
            if (playerData.getPlayer().hasPermission("core.chat.bypass")) return;
            e.setCancelled(true);
            playerData.getPlayer().sendMessage(Color.translate(msg.getString("mute")));
            return;
        }
        if (playerData.getPlayer().hasPermission("core.chat.delaybypass")) return;
        if (!playerData.getChatdelay().hasExpired()) {
            playerData.getPlayer().sendMessage(Color.translate(msg.getString("delay").replace("<time>", playerData.getChatdelay().getTimeMilisLeft()).replace("<left>", playerData.getChatdelay().getContextLeft())));
            e.setCancelled(true);
            return;
        }
        playerData.setChatdelay(cooldown);
    }
}
