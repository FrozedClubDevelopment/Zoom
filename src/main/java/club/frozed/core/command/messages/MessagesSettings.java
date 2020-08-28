package club.frozed.zoom.command.messages;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.manager.player.PlayerData;
import club.frozed.zoom.menu.chat.MessagesSettingsMenu;
import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessagesSettings extends BaseCMD {
    @Command(name = "messagessettings", aliases = {"msgsettings", "msettings"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            PlayerData playerData = PlayerData.getByUuid(p.getUniqueId());
            String defaultChatColor = ZoomPlugin.getInstance().getSettingsConfig().getConfig().getString("SETTINGS.CHAT.FORMAT.DEFAULT-COLOR");
            ChatColor CC;

            if (playerData.getChatColor() != null) {
                CC = ChatColor.valueOf(playerData.getChatColor());
            } else {
                CC = ChatColor.valueOf(defaultChatColor);
            }

            new MessagesSettingsMenu(CC).open(p);
        }
    }
}
