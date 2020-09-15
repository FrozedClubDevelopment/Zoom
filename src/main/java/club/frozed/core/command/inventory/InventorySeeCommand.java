package club.frozed.core.command.inventory;

import club.frozed.core.menu.invsee.InventorySeeMenu;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InventorySeeCommand extends BaseCMD {
    @Command(name = "inventorysee", permission = "core.command.invsee", aliases = {"invsee"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            p.sendMessage(CC.CHAT_BAR);
            p.sendMessage("§eUsage /" + cmd.getLabel() + " <player>");
            p.sendMessage(CC.CHAT_BAR);
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
