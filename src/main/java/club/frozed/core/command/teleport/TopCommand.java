package club.frozed.zoom.command.teleport;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.utils.Color;
import club.frozed.zoom.utils.Utils;
import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
import club.frozed.zoom.utils.config.ConfigCursor;
import org.bukkit.entity.Player;

public class TopCommand extends BaseCMD {
    @Command(name = "teleporttop", permission = "core.command.top", aliases = {"top"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(ZoomPlugin.getInstance().getMessagesConfig(), "teleport");
        if (args.length == 0) {
            p.teleport(Utils.teleportToTop(p.getLocation()));
            p.sendMessage(Color.translate(messages.getString("top")));
            return;
        }
    }
}
