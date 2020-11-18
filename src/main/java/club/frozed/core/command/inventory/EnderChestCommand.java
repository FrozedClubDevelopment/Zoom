package club.frozed.core.command.inventory;

import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import org.bukkit.entity.Player;

public class EnderChestCommand extends BaseCommand {
    @Command(name = "enderchest", permission = "core.command.enderchest", aliases = {"ec"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        p.openInventory(p.getEnderChest());
    }
}