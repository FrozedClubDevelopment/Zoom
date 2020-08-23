package me.ryzeon.core.command.inventory;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.command.Completer;
import me.ryzeon.core.utils.config.ConfigCursor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RepairCommand extends BaseCMD {
    @Completer(name = "repair", aliases = {"fix"})
    public List<String> FixComplete(CommandArgs args) {
        List<String> list = new ArrayList<String>();
        list.add("iteminhand");
        list.add("all");
        return list;
    }

    @Command(name = "repair", permission = "core.fix", aliases = {"fix"}, description = "To fix items", inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesconfig(), "fix");
        if (args.length == 0) {
            p.sendMessage(Color.LINE);
            p.sendMessage("§eUsage /" + cmd.getLabel() + "<all/iteminhand>");
            p.sendMessage(Color.LINE);
            return;
        }
        switch (args[0]) {
            case "iteminhand":
                ItemStack iteminhand = p.getItemInHand();
                Material material = Material.getMaterial(iteminhand.getTypeId());
                if (material == null || material == Material.AIR || material == Material.POTION || material == Material.GOLDEN_APPLE || material.isBlock() || material.getMaxDurability() < 1)
                    return;
                iteminhand.setDurability((short) 0);
                p.sendMessage(Color.translate(messages.getString("hand")));
                break;
            case "all":
                List<ItemStack> inventory = new ArrayList<>();
                for (ItemStack inventoryitem : p.getInventory().getContents()) {
                    if (inventoryitem != null && inventoryitem.getType() != Material.POTION && !inventoryitem.getType().isBlock() && inventoryitem.getType().getMaxDurability() > 1 && inventoryitem.getDurability() != 0) {
                        inventory.add(inventoryitem);
                    }
                }
                for (ItemStack armor : p.getInventory().getArmorContents()) {
                    if (armor != null && armor.getType() != Material.AIR) {
                        inventory.add(armor);
                    }
                }
                if (inventory.isEmpty()) {
                    p.sendMessage("§cNo Items to repair");
                    return;
                }
                for (ItemStack items : inventory) {
                    items.setDurability((short) 0);
                }
                p.sendMessage(Color.translate(messages.getString("all")));
                break;
            default:
                p.sendMessage(Color.LINE);
                p.sendMessage("§eUsage /" + cmd.getLabel() + "<all/iteminhand>");
                p.sendMessage(Color.LINE);
                break;
        }
    }
}
