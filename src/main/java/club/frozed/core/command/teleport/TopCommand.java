package club.frozed.core.command.teleport;

import club.frozed.core.Zoom;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.config.ConfigCursor;
import org.bukkit.entity.Player;

public class TopCommand extends BaseCMD {
    @Command(name = "teleporttop", permission = "core.command.top", aliases = {"top"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "teleport");
        if (args.length == 0) {
            p.teleport(Utils.teleportToTop(p.getLocation()));
            p.sendMessage(CC.translate(messages.getString("top")));
            return;
        }
    }
}
