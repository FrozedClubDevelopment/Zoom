package club.frozed.zoom.command.social;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.utils.Color;
import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
import club.frozed.zoom.utils.lang.Lang;
import org.bukkit.entity.Player;

public class StoreCommand extends BaseCMD {
    @Command(name = "store")

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        if (cmd.getArgs().length == 0) {
            p.sendMessage(Color.translate(ZoomPlugin.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.SOCIAL.MESSAGES")
                    .replace("<command>", "Store")
                    .replace("<social>", Lang.STORE))
            );
        }
    }
}
