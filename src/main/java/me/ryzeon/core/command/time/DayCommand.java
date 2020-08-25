package me.ryzeon.core.command.time;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.config.ConfigCursor;
import org.bukkit.entity.Player;

public class DayCommand extends BaseCMD {
    @Command(name = "day", permission = "core.command.day", inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesconfig(), "time");
        if (args.length == 0) {
            p.getWorld().setTime(0);
            p.sendMessage(Color.translate(messages.getString("day").replace("<world>", p.getWorld().getName())));
        }
    }
}
