package me.ryzeon.core.command.color;

import me.ryzeon.core.menu.color.ChatColorMenu;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
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
