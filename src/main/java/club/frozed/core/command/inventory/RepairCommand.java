package club.frozed.core.command.inventory;

import club.frozed.core.Zoom;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.command.Completer;
import club.frozed.core.utils.config.ConfigCursor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RepairCommand extends BaseCMD {
    @Completer(name = "repair", aliases = {"fix"})

    public List<String> FixComplete(CommandArgs args) {
        List<String> list = new ArrayList<>();
        list.add("iteminhand");
        list.add("all");
        return list;
    }

    @Command(name = "repair", permission = "core.command.fix", aliases = {"fix"}, description = "To fix items", inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "COMMANDS.FIX-MESSAGES");

        if (args.length == 0) {
            p.sendMessage(CC.CHAT_BAR);
            p.sendMessage("§eUsage /" + cmd.getLabel() + "<all/iteminhand>");
            p.sendMessage(CC.CHAT_BAR);
            return;
        }

        switch (args[0]) {
            case "iteminhand":
                ItemStack iteminhand = p.getItemInHand();
                Material material = Material.getMaterial(iteminhand.getTypeId());
                if (material == null || material == Material.AIR || material == Material.POTION || material == Material.GOLDEN_APPLE || material.isBlock() || material.getMaxDurability() < 1)
                    return;
                iteminhand.setDurability((short) 0);
                p.sendMessage(CC.translate(messages.getString("HAND")));
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
                p.sendMessage(CC.translate(messages.getString("ALL")));
                break;
            default:
                p.sendMessage(CC.CHAT_BAR);
                p.sendMessage("§eUsage /" + cmd.getLabel() + "<all/iteminhand>");
                p.sendMessage(CC.CHAT_BAR);
                break;
        }
    }
}
