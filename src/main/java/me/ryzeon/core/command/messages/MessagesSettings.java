package me.ryzeon.core.command.messages;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.manager.player.PlayerData;
import me.ryzeon.core.menu.chat.MessagesSettingsMenu;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
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
            String chatcolor = Zoom.getInstance().getSettingsconfig().getConfig().getString("chat.format.default-color");
            ChatColor chatColor;
            if (playerData.getChatColor() != null) {
                chatColor = ChatColor.valueOf(playerData.getChatColor());
            } else {
                chatColor = ChatColor.valueOf(chatcolor);
            }
            new MessagesSettingsMenu(chatColor).open(p);
        }
    }
}
