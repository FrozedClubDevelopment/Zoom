package club.frozed.core.command.teleport;

import club.frozed.core.Zoom;
import club.frozed.lib.chat.CC;
import club.frozed.core.utils.Utils;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import club.frozed.lib.config.ConfigCursor;
import org.bukkit.entity.Player;

public class TopCommand extends BaseCommand {
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
