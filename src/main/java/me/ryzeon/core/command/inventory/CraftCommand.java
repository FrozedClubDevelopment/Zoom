package me.ryzeon.core.command.inventory;

import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import org.bukkit.entity.Player;

public class CraftCommand extends BaseCMD {
    @Command(name = "craft", permission = "core.craft", inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        p.openWorkbench(p.getLocation(), true);
    }
}
