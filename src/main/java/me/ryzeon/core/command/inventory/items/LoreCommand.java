package me.ryzeon.core.command.inventory.items;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.NumberUtils;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.command.Completer;
import me.ryzeon.core.utils.config.ConfigCursor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LoreCommand extends BaseCMD {
    @Completer(name = "lore", aliases = {"setlore"})
    public List<String> LoreTab(CommandArgs args) {
        List<String> list = new ArrayList<String>();
        if (args.length() == 1) {
            list.add("add");
            list.add("remove");
        }
        return list;
    }

    @Command(name = "lore", permission = "core.lore", aliases = {"setlore"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesconfig(), "lore");
        if (args.length == 0) {
            p.sendMessage("§eUsage /" + cmd.getLabel() + " add/remove <text>");
            return;
        }
        ItemStack item = p.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            p.sendMessage("§cThe item cannot be null");
            return;
        }
        ItemMeta itemMeta = item.getItemMeta();
        // If lore no have add lore added a new arrayslist with lore
        if (!itemMeta.hasLore()) itemMeta.setLore(new ArrayList());
        switch (args[0]) {
            case "add": {
                List<String> lore = itemMeta.getLore();
                List<String> text = new ArrayList<>();
                for (int i = 1; i < args.length; i++) {
                    text.add(args[i]);
                }
                lore.add(StringUtils.join(text, " "));
                itemMeta.setLore(Color.translate(lore));
                item.setItemMeta(itemMeta);
                p.updateInventory();
                p.sendMessage(Color.translate(messages.getString("add")));
                break;
            }
            case "remove": {
                if (!NumberUtils.checkNumber(args[1])) {
                    p.sendMessage("§cIt must be a number");
                    return;
                }
                if (!itemMeta.hasLore() || Integer.parseInt(args[1]) >= itemMeta.getLore().size()) {
                    p.sendMessage("§eError in remove lore");
                    return;
                }
                List<String> lore = itemMeta.getLore();
                lore.remove(Integer.parseInt(args[1]));
                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);
                p.updateInventory();
                p.sendMessage(Color.translate(messages.getString("remove")));
            }
            break;
            default:
                p.sendMessage("§eUsage /" + cmd.getLabel() + " add/remove <text>");
                break;
        }
    }
}
