package club.frozed.core.command.messages;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.menu.chat.MessagesSettingsMenu;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessagesSettings extends BaseCommand {
    @Command(name = "messagessettings", aliases = {"msgsettings", "msettings", "chatsettings"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
            String defaultChatColor = Zoom.getInstance().getSettingsConfig().getConfiguration().getString("SETTINGS.CHAT.FORMAT.DEFAULT-COLOR");
            ChatColor CC;

            if (playerData.getChatColor() != null) {
                CC = ChatColor.valueOf(playerData.getChatColor());
            } else {
                CC = ChatColor.valueOf(defaultChatColor);
            }

            new MessagesSettingsMenu(CC).openMenu(player);
        }
    }
}
