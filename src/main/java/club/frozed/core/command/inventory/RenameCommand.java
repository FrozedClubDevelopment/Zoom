package club.frozed.core.command.inventory;

import club.frozed.core.Zoom;
import club.frozed.core.utils.Color;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.config.ConfigCursor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RenameCommand extends BaseCMD {
    @Command(name = "rename", permission = "core.command.rename", inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "COMMANDS");
        if (args.length == 0) {
            p.sendMessage("§eUsage /rename <name>");
            return;
        }
        ItemStack item = p.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            p.sendMessage("§cThe item cannot be null");
            return;
        }
        ItemMeta itemMeta = item.getItemMeta();
        List<String> text = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            text.add(args[i]);
        }
        itemMeta.setDisplayName(Color.translate(StringUtils.join(text, " ")));
        item.setItemMeta(itemMeta);
        p.updateInventory();
        p.sendMessage(Color.translate(messages.getString("RENAME").replace("<text>", StringUtils.join(text, " "))));
    }
}
