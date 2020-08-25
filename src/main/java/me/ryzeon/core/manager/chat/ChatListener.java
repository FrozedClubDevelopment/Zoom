package me.ryzeon.core.manager.chat;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.manager.database.redis.handler.Payload;
import me.ryzeon.core.manager.player.PlayerData;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.GsonUtil;
import me.ryzeon.core.utils.config.ConfigCursor;
import me.ryzeon.core.utils.config.ConfigReplacement;
import me.ryzeon.core.utils.lang.Lang;
import me.ryzeon.core.utils.time.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ChatListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        PlayerData playerData = PlayerData.getByUuid(e.getPlayer().getUniqueId());
        String message = e.getMessage();
        String messageformat = Zoom.getInstance().getSettingsconfig().getConfig().getString("chat.format.format");
        String chatcolor = Zoom.getInstance().getSettingsconfig().getConfig().getString("chat.format.default-color");
        boolean enabled = Zoom.getInstance().getSettingsconfig().getConfig().getBoolean("chat.format.enabled");
        if (!enabled) return;
        ConfigReplacement replacement = new ConfigReplacement(messageformat);
        replacement.add("<rank>", Color.translate("&7[&e+&7]"));
        if (playerData.getTag() == null) {
            replacement.add("<tag>", ChatColor.RESET);
        } else {
            replacement.add("<tag>", playerData.getTag());
        }
        if (playerData.getChatColor() != null) {
            replacement.add("<chatcolor>", ChatColor.valueOf(playerData.getChatColor()));
        } else {
            replacement.add("<chatcolor>", ChatColor.valueOf(chatcolor));
        }
        if (playerData.getNamecolor() != null) {
            replacement.add("<namecolor>", ChatColor.valueOf(playerData.getChatColor()));
        } else {
            replacement.add("<namecolor>", ChatColor.valueOf(chatcolor));
        }
        replacement.add("<name>", playerData.getPlayer().getName());
        replacement.add("<text>", ChatColor.stripColor(message));
        String format = replacement.toString();
        if (playerData.getPlayer().hasPermission("core.chatcolor.format")) {
            format = format.replace(message, Color.translate(message));
        }
        e.setFormat(format);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onStaffChat(AsyncPlayerChatEvent e) {
        PlayerData playerData = PlayerData.getByName(e.getPlayer().getName());
        ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getSettingsconfig(), "chat.staff-chat");
        boolean staffchat = playerData.isStaffchat();
        boolean adminchat = playerData.isAdminchat();
        String format = Color.translate(configCursor.getString("format")
                .replace("<server>", Lang.SERVER_NAME)
                .replace("<player>", playerData.getPlayer().getName())
                .replace("<text>", e.getMessage()));
        if (staffchat && !adminchat) {
            e.setCancelled(true);
            if (Zoom.getInstance().getRedis().isActive()) {
                Zoom.getInstance().getRedis().write(Payload.STAFF_CHAT, new GsonUtil()
                        .addProperty("SERVER", Lang.SERVER_NAME)
                        .addProperty("PLAYER", playerData.getPlayer().getName())
                        .addProperty("TEXT", e.getMessage()).get());
            } else {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission("core.staffchat")) {
                        p.sendMessage(format);
                    }
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
        String format = Color.translate(configCursor.getString("format")
                .replace("<server>", Lang.SERVER_NAME)
                .replace("<player>", playerData.getPlayer().getName())
                .replace("<text>", e.getMessage()));
        if (adminchat && !staffchat || adminchat && staffchat) {
            e.setCancelled(true);
            if (Zoom.getInstance().getRedis().isActive()) {
                Zoom.getInstance().getRedis().write(Payload.ADMIN_CHAT, new GsonUtil()
                        .addProperty("SERVER", Lang.SERVER_NAME)
                        .addProperty("PLAYER", playerData.getPlayer().getName())
                        .addProperty("TEXT", e.getMessage()).get());
            } else {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission("core.adminchat")) {
                        p.sendMessage(format);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onChatDelay(AsyncPlayerChatEvent e) {
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

    public String translate(String text, Player player, String message) {
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        text = text
                .replace("<rank>", "&7[&e+&7]")
                .replace("<tag>", playerData.getTag())
                .replace("<namecolor>", Color.translate("&b"))
                .replace("<name>", player.getName())
                .replace("<text>", message);
        if (player.hasPermission("core.chatcolor")) {
            text = text
                    .replace("<rank>", "NO HAY p")
                    .replace("<tag>", playerData.getTag())
                    .replace("<name>", player.getName())
                    .replace("<text>", Color.translate(message));
        }
        return text;
    }
}
