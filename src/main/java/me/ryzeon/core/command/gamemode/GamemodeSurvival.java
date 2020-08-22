package me.ryzeon.core.command.gamemode;

import me.ryzeon.core.utils.command.*;
import org.bukkit.entity.Player;

public class GamemodeSurvival extends BaseCMD {
    @Command(name = "gms",permission = "core.gamemode",aliases = {"gm0"},inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        if (args.length == 0){
            p.performCommand("gamemode survival");
        }
        else {
            String target = args[0];
            p.performCommand("gamemode " + target + " survival");
        }
    }
}
