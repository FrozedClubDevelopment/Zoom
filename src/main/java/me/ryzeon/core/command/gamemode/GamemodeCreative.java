package me.ryzeon.core.command.gamemode;

import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import org.bukkit.entity.Player;

public class GamemodeCreative extends BaseCMD {
    @Command(name = "gmc",permission = "core.gamemode",aliases = {"gm1"})
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        if (args.length == 0){
            p.performCommand("gamemode creative");
        }
        else {
            String target = args[0];
            p.performCommand("gamemode " + target + " creative");
        }
    }
}
