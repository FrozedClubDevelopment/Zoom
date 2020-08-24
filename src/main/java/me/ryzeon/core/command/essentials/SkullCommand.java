package me.ryzeon.core.command.essentials;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.Utils;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.config.ConfigCursor;
import me.ryzeon.core.utils.items.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SkullCommand extends BaseCMD {
    @Command(name = "skull", permission = "core.skull", aliases = {"cabeza", "getskull", "head"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesconfig(), "");
        if (args.length == 0) {
            p.sendMessage("§eUsage /" + cmd.getLabel() + " <player>");
            return;
        }
        List<String> asd = new ArrayList<>();
        asd.add(Color.LINE);
        asd.add("§f" + args[0] + "'s §e§lskull");
        asd.add(Color.LINE);
        ItemStack itemStack = new ItemCreator(Material.SKULL_ITEM, 3).setOwner(args[0]).setName("§e" + args[0] + "'s skull").setLore(asd).get();
        if (Utils.hasAvaliableSlot(p)) {
            p.getInventory().addItem(itemStack);
            p.sendMessage(Color.translate(messages.getString("skull").replace("<target>", args[0])));
        } else {
            p.sendMessage("§cYour inventory is full!");
        }
    }
}