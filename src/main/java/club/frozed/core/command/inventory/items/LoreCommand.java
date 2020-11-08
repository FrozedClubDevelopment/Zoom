package club.frozed.core.command.inventory.items;

import club.frozed.core.Zoom;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.NumberUtils;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.command.Completer;
import club.frozed.core.utils.config.ConfigCursor;
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
        List<String> list = new ArrayList<>();
        if (args.length() == 1) {
            list.add("add");
            list.add("remove");
        }

        return list;
    }

    @Command(name = "lore", permission = "core.command.lore", aliases = {"setlore"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "COMMANDS.LORE-MESSAGES");

        if (args.length == 0) {
            p.sendMessage("§cUsage: /" + cmd.getLabel() + " add/remove <text>");
            return;
        }

        ItemStack item = p.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            p.sendMessage("§cThe item cannot be null");
            return;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (!itemMeta.hasLore()) {
            itemMeta.setLore(new ArrayList());
        }

        List<String> lore = itemMeta.getLore();

        switch (args[0]) {
            case "add":
                List<String> text = new ArrayList<>();
                for (int i = 1; i < args.length; i++) {
                    text.add(args[i]);
                }
                lore.add(StringUtils.join(text, " "));
                itemMeta.setLore(CC.translate(lore));
                item.setItemMeta(itemMeta);
                p.updateInventory();
                p.sendMessage(CC.translate(messages.getString("ADD")));
                break;
            case "remove":
                if (!NumberUtils.checkNumber(args[1])) {
                    p.sendMessage("§cIt must be a number");
                    return;
                }
                if (!itemMeta.hasLore() || Integer.parseInt(args[1]) >= itemMeta.getLore().size()) {
                    p.sendMessage("§eError in remove lore");
                    return;
                }
                lore.remove(Integer.parseInt(args[1]));
                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);
                p.updateInventory();
                p.sendMessage(CC.translate(messages.getString("REMOVE")));
                break;
            default:
                p.sendMessage("§cUsage: /" + cmd.getLabel() + " add/remove <text>");
                break;
        }
    }
}
