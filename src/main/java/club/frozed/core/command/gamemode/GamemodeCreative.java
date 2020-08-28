package club.frozed.core.command.gamemode;

import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import org.bukkit.entity.Player;

public class GamemodeCreative extends BaseCMD {
    @Command(name = "gmc", permission = "core.gamemode.creative", aliases = {"gm1"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            p.performCommand("gamemode creative");
        } else {
            String target = args[0];
            p.performCommand("gamemode " + target + " creative");
        }
    }
}
