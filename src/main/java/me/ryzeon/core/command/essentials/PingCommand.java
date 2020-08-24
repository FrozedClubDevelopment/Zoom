package me.ryzeon.core.command.essentials;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.Utils;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PingCommand extends BaseCMD {
    @Command(name = "ping", aliases = {"ms", "conexion"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesconfig(), "ping");
        String sound = messages.getString("sound");
        int ping = Utils.getPing(p);
        if (args.length == 0) {
            p.sendMessage(Color.translate(messages.getString("default").replace("<ping>", String.valueOf(ping))));
            if (!(sound.equals("none") || sound.equals("NONE") || sound == null)) {
                p.playSound(p.getLocation(), Sound.valueOf(messages.getString("sound")), 2F, 2F);
            }
        }
        else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                p.sendMessage("§cCouldn't find player.");
                return;
            }
            int pingtarget = Utils.getPing(target);
            p.sendMessage(Color.translate(messages.getString("default")
                    .replace("<ping>", String.valueOf(ping))
                    .replace("<target>", target.getName())));
            Utils.sendPlayerSound(p, sound);
        }
    }
}
