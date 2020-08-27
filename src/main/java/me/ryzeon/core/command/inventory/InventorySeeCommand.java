package me.ryzeon.core.command.inventory;

import me.ryzeon.core.menu.invsee.InventorySeeMenu;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InventorySeeCommand extends BaseCMD {
    @Command(name = "inventorysee", permission = "core.command.invsee", aliases = {"invsee"})
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        if (args.length == 0) {
            p.sendMessage(Color.LINE);
            p.sendMessage("§eUsage /" + cmd.getLabel() + " <player>");
            p.sendMessage(Color.LINE);
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            p.sendMessage("§cThat players isn't online");
            return;
        }
        new InventorySeeMenu(target).open(p);
    }
}
