package club.frozed.core.command.chat;

import club.frozed.core.Zoom;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.utils.Color;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.config.ConfigCursor;
import org.bukkit.entity.Player;

public class StaffChatCommand extends BaseCMD {
    @Command(name = "staffChat", permission = "core.staffChat", aliases = {"sc"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getSettingsConfig(), "SETTINGS.STAFF-CHAT");
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        if (args.length == 0) {
            playerData.setStaffChat(!playerData.isStaffChat());
            String sound = configCursor.getString("SOUND");
            String status = (playerData.isStaffChat() ? "§aEnabled" : "§cDisabled");
            player.sendMessage(Color.translate(configCursor.getString("TOGGLE").replace("<status>", status)));
            Utils.sendPlayerSound(player, sound);
        }
    }
}
