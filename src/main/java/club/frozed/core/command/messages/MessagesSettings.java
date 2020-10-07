package club.frozed.core.command.messages;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.menu.chat.MessagesSettingsMenu;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
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
            String defaultChatColor = Zoom.getInstance().getSettingsConfig().getConfig().getString("SETTINGS.CHAT.FORMAT.DEFAULT-COLOR");
            ChatColor CC;

            if (playerData.getChatColor() != null) {
                CC = ChatColor.valueOf(playerData.getChatColor());
            } else {
                CC = ChatColor.valueOf(defaultChatColor);
            }

            new MessagesSettingsMenu(CC).openMenu(p);
        }
    }
}
