package club.frozed.core.command.essentials;

import club.frozed.core.Zoom;
import club.frozed.core.utils.Color;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PingCommand extends BaseCMD {
    @Command(name = "ping", aliases = {"ms", "conexion"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "COMMANDS");
        String sound = messages.getString("PING-MESSAGES.SOUND");

        int ping = Utils.getPing(p);

        if (args.length == 0) {
            p.sendMessage(Color.translate(messages.getString("PING-MESSAGES.DEFAULT").replace("<ping>", String.valueOf(ping))));
            if (!(sound.equalsIgnoreCase("none") || sound == null)) {
                p.playSound(p.getLocation(), Sound.valueOf(messages.getString("PING-MESSAGES.SOUND")), 2F, 2F);
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                p.sendMessage("§cCouldn't find player.");
                return;
            }

            int pingTarget = Utils.getPing(target);
            p.sendMessage(Color.translate(messages.getString("PING-MESSAGES.OTHER")
                    .replace("<ping>", String.valueOf(pingTarget))
                    .replace("<target>", target.getName())));
            Utils.sendPlayerSound(p, sound);
        }
    }
}
