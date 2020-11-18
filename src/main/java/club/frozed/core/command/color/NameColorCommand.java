package club.frozed.core.command.color;

import club.frozed.core.menu.color.namecolor.NameColorMenu;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import org.bukkit.entity.Player;

public class NameColorCommand extends BaseCommand {
    @Command(name = "color", permission = "core.nameColor", aliases = {"nameColor"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            new NameColorMenu().openMenu(p);
        }
    }
}
