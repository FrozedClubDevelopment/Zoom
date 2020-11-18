package club.frozed.core.command.color;

import club.frozed.core.menu.color.chatcolor.ChatColorMenu;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import org.bukkit.entity.Player;

public class ChatColorCommand extends BaseCommand {
    @Command(name = "chatcolor", aliases = {"cc"}, permission = "core.chatcolor", inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            new ChatColorMenu().openMenu(p);
        }
    }
}
