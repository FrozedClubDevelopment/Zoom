package club.frozed.zoom.command.inventory;

import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
import org.bukkit.entity.Player;

public class EnderChestCommand extends BaseCMD {
    @Command(name = "enderchest", permission = "core.command.enderchest", aliases = {"ec"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        p.openInventory(p.getEnderChest());
    }
}