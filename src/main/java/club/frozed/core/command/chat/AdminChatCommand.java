package club.frozed.core.command.chat;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.utils.Utils;
import club.frozed.lib.chat.CC;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.config.ConfigCursor;
import org.bukkit.entity.Player;

public class AdminChatCommand extends BaseCommand {
    @Command(name = "adminChat", permission = "core.adminChat", aliases = {"ac"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getSettingsConfig(), "SETTINGS.ADMIN-CHAT");
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        if (args.length == 0) {
            playerData.setAdminChat(!playerData.isAdminChat());
            String sound = configCursor.getString("SOUND");
            String status = (playerData.isAdminChat() ? "§aEnabled" : "§cDisabled");
            player.sendMessage(CC.translate(configCursor.getString("TOGGLE").replace("<status>", status)));
            Utils.sendPlayerSound(player, sound);
        }
    }
}
