package club.frozed.core.command.essentials;

import club.frozed.core.Zoom;
import club.frozed.core.utils.Color;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.config.ConfigCursor;
import club.frozed.core.utils.items.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SkullCommand extends BaseCMD {
    @Command(name = "skull", permission = "core.essentials.skull", aliases = {"cabeza", "getskull", "head"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "COMMANDS");

        if (args.length == 0) {
            p.sendMessage("§eUsage /" + cmd.getLabel() + " <player>");
            return;
        }

        ItemStack itemStack = new ItemCreator(Material.SKULL_ITEM, 3).setOwner(args[0]).setName("§e" + args[0] + "'s skull").get();
        if (Utils.hasAvaliableSlot(p)) {
            p.getInventory().addItem(itemStack);
            p.sendMessage(Color.translate(messages.getString("SKULL-MESSAGE").replace("<target>", args[0])));
        } else {
            p.sendMessage("§cYour inventory is full!");
        }
    }
}