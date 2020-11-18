package club.frozed.core.command.inventory;

import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import org.bukkit.entity.Player;

public class CraftCommand extends BaseCommand {
    @Command(name = "craft", permission = "core.command.craft", inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        p.openWorkbench(p.getLocation(), true);
    }
}
