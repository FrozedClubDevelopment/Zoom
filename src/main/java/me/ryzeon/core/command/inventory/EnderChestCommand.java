package me.ryzeon.core.command.inventory;

import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import org.bukkit.entity.Player;

public class EnderChestCommand extends BaseCMD {
    @Command(name = "enderchest", permission = "core.command.enderchest", aliases = {"ec"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        p.openInventory(p.getEnderChest());
    }
}