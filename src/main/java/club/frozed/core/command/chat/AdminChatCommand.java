package club.frozed.zoom.command.chat;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.manager.player.PlayerData;
import club.frozed.zoom.utils.Color;
import club.frozed.zoom.utils.Utils;
import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
import club.frozed.zoom.utils.config.ConfigCursor;
import org.bukkit.entity.Player;

public class AdminChatCommand extends BaseCMD {
    @Command(name = "adminChat", permission = "core.adminChat", aliases = {"ac"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor configCursor = new ConfigCursor(ZoomPlugin.getInstance().getSettingsConfig(), "SETTINGS.ADMIN-CHAT");
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        if (args.length == 0) {
            playerData.setAdminChat(!playerData.isAdminChat());
            String sound = configCursor.getString("SOUND");
            String status = (playerData.isAdminChat() ? "§aEnabled" : "§cDisabled");
            player.sendMessage(Color.translate(configCursor.getString("TOGGLE").replace("<status>", status)));
            Utils.sendPlayerSound(player, sound);
        }
    }
}
