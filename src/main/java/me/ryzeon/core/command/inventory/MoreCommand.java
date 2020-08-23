package me.ryzeon.core.command.inventory;

import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MoreCommand extends BaseCMD {
    @Command(name = "more", permission = "core.more", inGameOnly = true, aliases = {"stackitem"})
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        ItemStack item = p.getItemInHand();
        if (item == null || item.getType() == Material.AIR) return;
        if (item.getAmount() >= 64) return;
        item.setAmount(64);
        p.updateInventory();
        p.sendMessage("§eYou stacked your item.");
    }
}
