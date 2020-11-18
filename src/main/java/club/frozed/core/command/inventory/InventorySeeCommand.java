package club.frozed.core.command.inventory;

import club.frozed.core.menu.invsee.InventorySeeMenu;
import club.frozed.lib.chat.CC;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InventorySeeCommand extends BaseCommand {
    @Command(name = "inventorysee", permission = "core.command.invsee", aliases = {"invsee"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            p.sendMessage(CC.CHAT_BAR);
            p.sendMessage("§cUsage: /" + cmd.getLabel() + " <player>");
            p.sendMessage(CC.CHAT_BAR);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            p.sendMessage("§cThat players isn't online");
            return;
        }
        new InventorySeeMenu(target).openMenu(p);
    }
}
