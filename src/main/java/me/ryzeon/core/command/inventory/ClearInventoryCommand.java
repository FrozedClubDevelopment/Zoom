package me.ryzeon.core.command.inventory;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ClearInventoryCommand extends BaseCMD {
    @Command(name = "clear", permission = "core.command.clear", usage = "Clear inventory", inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesconfig(), "clear");
        if (args.length == 0) {
            cleanInventory(p);
            p.sendMessage(Color.translate(messages.getString("default")));
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                p.sendMessage("§cPlayers isn't online");
                return;
            }
            cleanInventory(target);
            target.sendMessage(Color.translate(messages.getString("other.target").replace("<player>", p.getDisplayName())));
            p.sendMessage(Color.translate(messages.getString("other.sender")).replace("<target>", target.getDisplayName()));
        }
    }

    public void cleanInventory(Player p) {
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
    }
}
