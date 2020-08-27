package me.ryzeon.core.command.color;

import me.ryzeon.core.menu.color.NameColorMenu;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import org.bukkit.entity.Player;

public class NameColorCommand extends BaseCMD {
    @Command(name = "color", permission = "core.namecolor", aliases = {"namecolor"})
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        if (args.length == 0) {
            new NameColorMenu().open(p);
        }
    }
}
