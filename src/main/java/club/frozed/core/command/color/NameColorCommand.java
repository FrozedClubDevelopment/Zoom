package club.frozed.core.command.color;

import club.frozed.core.menu.color.NameColorMenu;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
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
