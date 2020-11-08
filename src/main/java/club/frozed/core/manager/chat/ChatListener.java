package club.frozed.core.manager.chat;

import club.frozed.core.Zoom;
import club.frozed.core.manager.database.redis.payload.Payload;
import club.frozed.core.manager.database.redis.payload.RedisMessage;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.punishments.Punishment;
import club.frozed.core.manager.player.punishments.PunishmentType;
import club.frozed.core.manager.staff.StaffLang;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.config.ConfigCursor;
import club.frozed.core.utils.config.ConfigReplacement;
import club.frozed.core.utils.lang.Lang;
import club.frozed.core.utils.punishment.PunishmentUtil;
import club.frozed.core.utils.time.Cooldown;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Objects;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        PlayerData playerData = PlayerData.getPlayerData(e.getPlayer().getUniqueId());
        String message = e.getMessage();
        String messageFormat = Zoom.getInstance().getSettingsConfig().getConfig().getString("SETTINGS.CHAT.FORMAT.FORMAT");
        String chatColor = Zoom.getInstance().getSettingsConfig().getConfig().getString("SETTINGS.CHAT.FORMAT.DEFAULT-COLOR");
        boolean enabled = Zoom.getInstance().getSettingsConfig().getConfig().getBoolean("SETTINGS.CHAT.FORMAT.ENABLED");
        if (!enabled) return;

        ConfigReplacement replacement = new ConfigReplacement(messageFormat);
        replacement.add("<rank>", CC.translate(playerData.getHighestRank().getPrefix()));
        if (playerData.getTag() != null) {
            replacement.add("<tag>", " " + playerData.getTag() + " ");
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
            replacement.add("<nameColor>", playerData.getHighestRank().getColor());
        }

        replacement.add("<name>", playerData.getHighestRank().formatName(e.getPlayer()));
        replacement.add("<text>", ChatColor.stripColor(message));

        String format = replacement.toString();
        if (playerData.getPlayer().hasPermission("core.chatcolor.format")) {
            format = format.replace(message, CC.translate(message));
        }

        e.setFormat(format.replace("%", "%%").replace("\\$", "\\\\\\$"));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMuteChat(AsyncPlayerChatEvent event) {
        PlayerData data = PlayerData.getPlayerData(event.getPlayer().getUniqueId());
        if (data == null) return;
        Punishment punishment = data.getActivePunishment(PunishmentType.MUTE);
        if (punishment != null) {
            if (punishment.isLifetime()) {
                Zoom.getInstance().getPunishmentConfig().getConfig().getStringList("PUNISHMENT-MESSAGES.PLAYER.CHAT.PERMANENT").forEach(text -> {
                    data.getPlayer().sendMessage(CC.translate(text)
                            .replace("<mute-time>", punishment.getTimeLeft(true))
                            .replace("<reason>", PunishmentUtil.getPunishReason(punishment)));
                });
            } else {
                Zoom.getInstance().getPunishmentConfig().getConfig().getStringList("PUNISHMENT-MESSAGES.PLAYER.CHAT.TEMP").forEach(text -> {
                    data.getPlayer().sendMessage(CC.translate(text)
                            .replace("<mute-time>", punishment.getTimeLeft(true))
                            .replace("<reason>", PunishmentUtil.getPunishReason(punishment)));
                });
            }
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onStaffChat(AsyncPlayerChatEvent e) {
        PlayerData playerData = PlayerData.getPlayerData(e.getPlayer().getUniqueId());
        ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getSettingsConfig(), "SETTINGS.STAFF-CHAT");
        boolean staffChat = Objects.requireNonNull(playerData).isStaffChat();
        boolean adminChat = playerData.isAdminChat();
        String format = CC.translate(configCursor.getString("FORMAT")
                .replace("<server>", Lang.SERVER_NAME) // ERROR
                .replace("<player>", playerData.getPlayer().getName())
                .replace("<text>", e.getMessage()));
        if (staffChat && !adminChat) {
            e.setCancelled(true);
            if (Zoom.getInstance().getRedisManager().isActive()) {
                String json = new RedisMessage(Payload.STAFF_CHAT)
                        .setParam("SERVER", Lang.SERVER_NAME)
                        .setParam("PLAYER", playerData.getPlayer().getName())
                        .setParam("TEXT", e.getMessage()).toJSON();
                Zoom.getInstance().getRedisManager().write(json);
            } else {
                StaffLang.sendStaffChat(format);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onAdminChat(AsyncPlayerChatEvent e) {
        PlayerData playerData = PlayerData.getPlayerData(e.getPlayer().getUniqueId());
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
                        .setParam("SERVER", Lang.SERVER_NAME)
                        .setParam("PLAYER", playerData.getPlayer().getName())
                        .setParam("TEXT", e.getMessage()).toJSON();
                Zoom.getInstance().getRedisManager().write(json);
            } else {
                StaffLang.sendAdminChat(format);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onChatDelay(AsyncPlayerChatEvent e) {
        PlayerData playerData = PlayerData.getPlayerData(e.getPlayer().getUniqueId());
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
            playerData.getPlayer().sendMessage(CC.translate(msg.getString("DELAY")
                    .replace("<time>", playerData.getChatDelay().getTimeMilisLeft())
                    .replace("<left>", playerData.getChatDelay().getContextLeft()))
            );
            e.setCancelled(true);
            return;
        }
        playerData.setChatDelay(cooldown);
    }

    @EventHandler
    public void onCommandReplace(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/msg") || event.getMessage().startsWith("/tell") || event.getMessage().startsWith("TELL") || event.getMessage().startsWith("/MSG")) {
            event.setCancelled(true);
            event.getPlayer().chat(event.getMessage()
                    .replace("/msg", "/message")
                    .replace("/MSG", "/message")
                    .replace("/TELL", "/message")
                    .replace("/tell", "/message"));
        }
        if (event.getMessage().startsWith("/kick") || event.getMessage().startsWith("/KICK")){
            event.setCancelled(true);
            event.getPlayer().chat(event.getMessage()
                    .replace("/kick", "/kickear")
                    .replace("/KICK", "/kickear")
            );
        }
    }

    public String translate(String text, Player player, String message) {
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        text = text
                .replace("<rank>", CC.translate(playerData.getHighestRank().getPrefix()))
                .replace("<tag>", playerData.getTag())
                .replace("<nameColor>", "&" + ChatColor.valueOf(playerData.getChatColor()).getChar())
                .replace("<name>", player.getName())
                .replace("<text>", message);

        if (player.hasPermission("core.chatcolor")) {
            text = text
                    .replace("<rank>", CC.translate(playerData.getHighestRank().getPrefix()))
                    .replace("<tag>", playerData.getTag())
                    .replace("<nameColor>", "&" + ChatColor.valueOf(playerData.getChatColor()).getChar())
                    .replace("<name>", player.getName())
                    .replace("<text>", CC.translate(message));
        }
        return text;
    }
}
