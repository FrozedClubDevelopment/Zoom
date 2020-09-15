package club.frozed.core.manager.chat;

import club.frozed.core.Zoom;
import club.frozed.core.manager.database.redis.payload.Payload;
import club.frozed.core.manager.database.redis.payload.RedisMessage;
import club.frozed.core.manager.staff.StaffLang;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.config.ConfigCursor;
import club.frozed.core.utils.config.ConfigReplacement;
import club.frozed.core.utils.lang.Lang;
import club.frozed.core.utils.time.Cooldown;
import club.frozed.core.manager.player.PlayerData;
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
        String messageFormat = Zoom.getInstance().getSettingsConfig().getConfig().getString("SETTINGS.CHAT.FORMAT.FORMAT");
        String chatColor = Zoom.getInstance().getSettingsConfig().getConfig().getString("SETTINGS.CHAT.FORMAT.DEFAULT-COLOR");
        boolean enabled = Zoom.getInstance().getSettingsConfig().getConfig().getBoolean("SETTINGS.CHAT.FORMAT.ENABLED");
        if (!enabled) return;

        ConfigReplacement replacement = new ConfigReplacement(messageFormat);
        replacement.add("<rank>", CC.translate("&7[&e+&7]"));
        if (playerData.getTag() != null) {
            replacement.add("<tag>", " " + playerData.getTag());
        } else {
            replacement.add("<tag>", "");
        }
        if (playerData.getChatColor() != null) {
            replacement.add("<chatcolor>", ChatColor.valueOf(playerData.getChatColor()));
        } else {
            replacement.add("<chatcolor>", ChatColor.valueOf(chatColor));
        }
        if (playerData.getNameColor() != null) {
            if (playerData.isItalic() && playerData.isBold()) {
                replacement.add("<nameColor>", ChatColor.valueOf(playerData.getNameColor()) + "§l§o");
            } else if (playerData.isBold() && playerData.isItalic()) {
                replacement.add("<nameColor>", ChatColor.valueOf(playerData.getNameColor()) + "§l§o");
            } else if (playerData.isItalic()) {
                replacement.add("<nameColor>", ChatColor.valueOf(playerData.getNameColor()) + "§o");
            } else if (playerData.isBold()) {
                replacement.add("<nameColor>", ChatColor.valueOf(playerData.getNameColor()) + "§l");
            } else {
                replacement.add("<nameColor>", ChatColor.valueOf(playerData.getNameColor()));
            }
        } else {
            replacement.add("<nameColor>", ChatColor.valueOf(chatColor));
        }

        replacement.add("<name>", playerData.getPlayer().getName());
        replacement.add("<text>", ChatColor.stripColor(message));

        String format = replacement.toString();
        if (playerData.getPlayer().hasPermission("core.chatcolor.format")) {
            format = format.replace(message, CC.translate(message));
        }
        e.setFormat(format);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onStaffChat(AsyncPlayerChatEvent e) {
        PlayerData playerData = PlayerData.getByName(e.getPlayer().getName());
        ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getSettingsConfig(), "SETTINGS.STAFF-CHAT");
        boolean staffChat = playerData.isStaffChat();
        boolean adminChat = playerData.isAdminChat();
        String format = CC.translate(configCursor.getString("FORMAT")
                .replace("<server>", Lang.SERVER_NAME) // ERROR
                .replace("<player>", playerData.getPlayer().getName())
                .replace("<text>", e.getMessage()));
        if (staffChat && !adminChat) {
            e.setCancelled(true);
            if (Zoom.getInstance().getRedisManager().isActive()) {
                String json = new RedisMessage(Payload.STAFF_CHAT)
                        .setParam("SERVER",Lang.SERVER_NAME)
                        .setParam("PLAYER",playerData.getPlayer().getName())
                        .setParam("TEXT",e.getMessage()).toJSON();
                Zoom.getInstance().getRedisManager().write(json);
            } else {
                StaffLang.sendStaffChat(format);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onAdminChat(AsyncPlayerChatEvent e) {
        PlayerData playerData = PlayerData.getByUuid(e.getPlayer().getUniqueId());
        ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getSettingsConfig(), "SETTINGS.ADMIN-CHAT");
        boolean staffChat = playerData.isStaffChat();
        boolean adminChat = playerData.isAdminChat();
        String format = CC.translate(configCursor.getString("FORMAT")
                .replace("<server>", Lang.SERVER_NAME) // ERROR
                .replace("<player>", playerData.getPlayer().getName())
                .replace("<text>", e.getMessage()));
        if (adminChat && !staffChat || adminChat && staffChat) {
            e.setCancelled(true);
            if (Zoom.getInstance().getRedisManager().isActive()) {
                String json = new RedisMessage(Payload.ADMIN_CHAT)
                        .setParam("SERVER",Lang.SERVER_NAME)
                        .setParam("PLAYER",playerData.getPlayer().getName())
                        .setParam("TEXT",e.getMessage()).toJSON();
                Zoom.getInstance().getRedisManager().write(json);
            } else {
                StaffLang.sendAdminChat(format);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onChatDelay(AsyncPlayerChatEvent e) {
        PlayerData playerData = PlayerData.getByUuid(e.getPlayer().getUniqueId());
        ChatManager chatManager = Zoom.getInstance().getChatManager();
        ConfigCursor msg = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "NETWORK.CHAT-MANAGER.PLAYER-MESSAGE");
        Cooldown cooldown = new Cooldown(chatManager.getDelay());

        if (chatManager.isMute()) {
            if (playerData.getPlayer().hasPermission("core.chat.bypass")) return;
            e.setCancelled(true);
            playerData.getPlayer().sendMessage(CC.translate(msg.getString("MUTE")));
            return;
        }

        if (playerData.getPlayer().hasPermission("core.chat.delaybypass")) return;

        if (!playerData.getChatDelay().hasExpired()) {
            playerData.getPlayer().sendMessage(CC.translate(msg.getString("DELAY").replace("<time>", playerData.getChatDelay().getTimeMilisLeft())
                            .replace("<left>", playerData.getChatDelay().getContextLeft()))
            );
            e.setCancelled(true);
            return;
        }
        playerData.setChatDelay(cooldown);
    }

    public String translate(String text, Player player, String message) {
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        text = text
                .replace("<rank>", "&7[&e+&7]")
                .replace("<tag>", playerData.getTag())
                .replace("<nameColor>", CC.translate("&b"))
                .replace("<name>", player.getName())
                .replace("<text>", message);

        if (player.hasPermission("core.chatcolor")) {
            text = text
                    .replace("<rank>", "Not Available")
                    .replace("<tag>", playerData.getTag())
                    .replace("<name>", player.getName())
                    .replace("<text>", CC.translate(message));
        }
        return text;
    }
}
