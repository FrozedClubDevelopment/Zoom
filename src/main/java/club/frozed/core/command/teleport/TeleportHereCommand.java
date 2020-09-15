package club.frozed.core.command.teleport;

import club.frozed.core.Zoom;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeleportHereCommand extends BaseCMD {
    @Command(name = "teleporthere", permission = "core.command.tphere", aliases = {"tphere", "s"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "COMMANDS.TELEPORT-MESSAGES.TPHERE");

        if (args.length == 0) {
            p.sendMessage("§cUsage /" + cmd.getLabel() + " <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            p.sendMessage("§cPlayer isn't online");
            return;
        }

        target.teleport(p.getLocation());
        p.sendMessage(CC.translate(messages.getString("SENDER").replace("<target>", target.getName())));
        target.sendMessage(CC.translate(messages.getString("TARGET").replace("<player>", p.getName())));
    }
}
