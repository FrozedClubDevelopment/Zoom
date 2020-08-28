package club.frozed.zoom.command.inventory;

import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
import org.bukkit.entity.Player;

public class CraftCommand extends BaseCMD {
    @Command(name = "craft", permission = "core.command.craft", inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        p.openWorkbench(p.getLocation(), true);
    }
}
