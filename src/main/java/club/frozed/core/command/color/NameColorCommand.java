package club.frozed.zoom.command.color;

import club.frozed.zoom.menu.color.NameColorMenu;
import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
import org.bukkit.entity.Player;

public class NameColorCommand extends BaseCMD {
    @Command(name = "color", permission = "core.nameColor", aliases = {"nameColor"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            new NameColorMenu().open(p);
        }
    }
}
