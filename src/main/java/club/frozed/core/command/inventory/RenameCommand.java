package club.frozed.core.command.inventory;

import club.frozed.core.Zoom;
import club.frozed.lib.chat.CC;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import club.frozed.lib.config.ConfigCursor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RenameCommand extends BaseCommand {
    @Command(name = "rename", permission = "core.command.rename", inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "COMMANDS");
        if (args.length == 0) {
            p.sendMessage("§cUsage: /rename <name>");
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
        itemMeta.setDisplayName(CC.translate(StringUtils.join(text, " ")));
        item.setItemMeta(itemMeta);
        p.updateInventory();
        p.sendMessage(CC.translate(messages.getString("RENAME").replace("<text>", StringUtils.join(text, " "))));
    }
}
