package club.frozed.zoom.manager.chat;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.utils.Color;
import club.frozed.zoom.utils.config.ConfigCursor;
import club.frozed.zoom.utils.config.ConfigReplacement;
import club.frozed.zoom.utils.lang.Lang;
import club.frozed.zoom.utils.time.Cooldown;
import club.frozed.zoom.manager.player.PlayerData;
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
        String messageFormat = ZoomPlugin.getInstance().getSettingsConfig().getConfig().getString("CHAT.FORMAT.FORMAT");
        String chatColor = ZoomPlugin.getInstance().getSettingsConfig().getConfig().getString("CHAT.FORMAT.DEFAULT-COLOR");
        boolean enabled = ZoomPlugin.getInstance().getSettingsConfig().getConfig().getBoolean("CHAT.FORMAT.ENABLED");
        if (!enabled) return;

        ConfigReplacement replacement = new ConfigReplacement(messageFormat);
        replacement.add("<rank>", Color.translate("&7[&e+&7]"));
        if (playerData.getTag() == null) {
            replacement.add("<tag>", "");
        } else {
            replacement.add("<tag>", " " + playerData.getTag());
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
            format = format.replace(message, Color.translate(message));
        }
        e.setFormat(format);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onStaffChat(AsyncPlayerChatEvent e) {
        PlayerData playerData = PlayerData.getByName(e.getPlayer().getName());
        ConfigCursor configCursor = new ConfigCursor(ZoomPlugin.getInstance().getSettingsConfig(), "CHAT.STAFF-CHAT");
        boolean staffChat = playerData.isStaffChat();
        boolean adminChat = playerData.isAdminChat();
        String format = Color.translate(configCursor.getString("FORMAT")
                .replace("<server>", Lang.SERVER_NAME)
                .replace("<player>", playerData.getPlayer().getName())
                .replace("<text>", e.getMessage()));
//        if (staffChat && !adminChat) {
//            e.setCancelled(true);
//            if (ZoomPlugin.getInstance().getRedis().isActive()) {
//                ZoomPlugin.getInstance().getRedis().write("STAFF_CHAT", new JsonUtil()
//                        .addProperty("SERVER", Lang.SERVER_NAME)
//                        .addProperty("PLAYER", playerData.getPlayer().getName())
//                        .addProperty("TEXT", e.getMessage()).get());
//            } else {
//                for (Player p : Bukkit.getOnlinePlayers()) {
//                    if (p.hasPermission("core.staffChat")) {
//                        p.sendMessage(format);
//                    }
//                }
//            }
//        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onAdminChat(AsyncPlayerChatEvent e) {
        PlayerData playerData = PlayerData.getByUuid(e.getPlayer().getUniqueId());
        ConfigCursor configCursor = new ConfigCursor(ZoomPlugin.getInstance().getSettingsConfig(), "CHAT.ADMIN-CHAT");
        boolean staffChat = playerData.isStaffChat();
        boolean adminChat = playerData.isAdminChat();
        String format = Color.translate(configCursor.getString("FORMAT")
                .replace("<server>", Lang.SERVER_NAME)
                .replace("<player>", playerData.getPlayer().getName())
                .replace("<text>", e.getMessage()));
        if (adminChat && !staffChat || adminChat && staffChat) {
            e.setCancelled(true);
//            if (ZoomPlugin.getInstance().getRedis().isActive()) {
//                ZoomPlugin.getInstance().getRedis().write("ADMIN_CHAT", new GsonUtil()
//                        .addProperty("SERVER", Lang.SERVER_NAME)
//                        .addProperty("PLAYER", playerData.getPlayer().getName())
//                        .addProperty("TEXT", e.getMessage()).get());
//            } else {
//                for (Player p : Bukkit.getOnlinePlayers()) {
//                    if (p.hasPermission("core.adminChat")) {
//                        p.sendMessage(format);
//                    }
//                }
//            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onChatDelay(AsyncPlayerChatEvent e) {
        PlayerData playerData = PlayerData.getByUuid(e.getPlayer().getUniqueId());
        ChatManager chatManager = ZoomPlugin.getInstance().getChatManager();
        ConfigCursor msg = new ConfigCursor(ZoomPlugin.getInstance().getMessagesConfig(), "CHAT-MANAGER.PLAYER-MESSAGE");
        Cooldown cooldown = new Cooldown(chatManager.getDelay());

        if (chatManager.isMute()) {
            if (playerData.getPlayer().hasPermission("core.chat.bypass")) return;
            e.setCancelled(true);
            playerData.getPlayer().sendMessage(Color.translate(msg.getString("MUTE")));
            return;
        }

        if (playerData.getPlayer().hasPermission("core.chat.delaybypass")) return;

        if (!playerData.getChatDelay().hasExpired()) {
            playerData.getPlayer().sendMessage(Color.translate(msg.getString("DELAY").replace("<time>", playerData.getChatDelay().getTimeMilisLeft())
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
                .replace("<nameColor>", Color.translate("&b"))
                .replace("<name>", player.getName())
                .replace("<text>", message);

        if (player.hasPermission("core.chatcolor")) {
            text = text
                    .replace("<rank>", "Not Available")
                    .replace("<tag>", playerData.getTag())
                    .replace("<name>", player.getName())
                    .replace("<text>", Color.translate(message));
        }
        return text;
    }
}
