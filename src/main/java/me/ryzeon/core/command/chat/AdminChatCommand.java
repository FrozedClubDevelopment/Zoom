package me.ryzeon.core.command.chat;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.manager.player.PlayerData;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.Utils;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.config.ConfigCursor;
import org.bukkit.entity.Player;

public class AdminChatCommand extends BaseCMD {
    @Command(name = "adminchat", permission = "core.adminchat", aliases = {"ac"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getSettingsconfig(), "chat.admin-chat");
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        if (args.length == 0) {
            playerData.setAdminchat(!playerData.isAdminchat());
            String sound = configCursor.getString("sound");
            String status = (playerData.isAdminchat() ? "§aEnabled" : "§cDisabled");
            player.sendMessage(Color.translate(configCursor.getString("toggle").replace("<status>", status)));
            Utils.sendPlayerSound(player, sound);
        }
    }
}
