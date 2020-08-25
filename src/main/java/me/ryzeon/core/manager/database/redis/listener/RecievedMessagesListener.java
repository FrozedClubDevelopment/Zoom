package me.ryzeon.core.manager.database.redis.listener;

import lombok.Getter;
import me.ryzeon.core.Zoom;
import me.ryzeon.core.manager.database.redis.implement.AdminChatMessage;
import me.ryzeon.core.manager.database.redis.implement.ServerManager;
import me.ryzeon.core.manager.database.redis.message.Message;
import me.ryzeon.core.manager.database.redis.message.MessageListener;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
public class RecievedMessagesListener implements MessageListener {
    @Message(id = "ADMIN_CHAT")
    public void onAdminChatMessage(AdminChatMessage message) {
        ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getSettingsconfig(), "chat.admin-chat");
        String fulltext = Color.translate(configCursor.getString("format")
                .replace("<server>", message.getServer())
                .replace("<player>", message.getPlayername()))
                .replace("<text>", Color.translate(message.getMessage()));
        for (Player playes : Bukkit.getOnlinePlayers()) {
            if (playes.hasPermission("core.adminchat")) {
                playes.sendMessage(fulltext);
            }
        }
    }

    @Message(id = "SERVER_ON")
    public void onServerManager(ServerManager manager) {
        Bukkit.broadcastMessage("EL SERVER" + manager + "estaraa " + manager + " en 5 s");
    }
}
