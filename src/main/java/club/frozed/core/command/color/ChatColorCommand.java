package club.frozed.zoom.command.color;

import club.frozed.zoom.menu.color.ChatColorMenu;
import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
import org.bukkit.entity.Player;

public class ChatColorCommand extends BaseCMD {
    @Command(name = "chatcolor", aliases = {"cc"}, permission = "core.chatcolor", inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            new ChatColorMenu().open(p);
        }
    }
}
