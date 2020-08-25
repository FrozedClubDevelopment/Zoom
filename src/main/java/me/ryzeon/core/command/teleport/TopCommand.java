package me.ryzeon.core.command.teleport;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.Utils;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.config.ConfigCursor;
import org.bukkit.entity.Player;

public class TopCommand extends BaseCMD {
    @Command(name = "teleporttop", permission = "core.command.top", aliases = {"top"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesconfig(), "teleport");
        if (args.length == 0) {
            p.teleport(Utils.teleportToTop(p.getLocation()));
            p.sendMessage(Color.translate(messages.getString("top")));
            return;
        }
    }
}
