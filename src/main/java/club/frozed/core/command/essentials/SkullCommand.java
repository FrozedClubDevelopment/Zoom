package club.frozed.core.command.essentials;

import club.frozed.core.Zoom;
import club.frozed.lib.chat.CC;
import club.frozed.core.utils.Utils;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import club.frozed.lib.config.ConfigCursor;
import club.frozed.lib.item.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SkullCommand extends BaseCommand {
    @Command(name = "skull", permission = "core.essentials.skull", aliases = {"cabeza", "getskull", "head"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "COMMANDS");

        if (args.length == 0) {
            p.sendMessage("§cUsage: /" + cmd.getLabel() + " <player>");
            return;
        }

        ItemStack itemStack = new ItemCreator(Material.SKULL_ITEM, 3).setOwner(args[0]).setName("§e" + args[0] + "'s skull").get();
        if (Utils.hasAvailableSlot(p)) {
            p.getInventory().addItem(itemStack);
            p.sendMessage(CC.translate(messages.getString("SKULL-MESSAGE").replace("<target>", args[0])));
        } else {
            p.sendMessage("§cYour inventory is full!");
        }
    }
}