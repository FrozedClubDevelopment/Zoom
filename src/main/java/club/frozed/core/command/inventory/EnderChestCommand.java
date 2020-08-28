package club.frozed.core.command.inventory;

import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import org.bukkit.entity.Player;

public class EnderChestCommand extends BaseCMD {
    @Command(name = "enderchest", permission = "core.command.enderchest", aliases = {"ec"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        p.openInventory(p.getEnderChest());
    }
}