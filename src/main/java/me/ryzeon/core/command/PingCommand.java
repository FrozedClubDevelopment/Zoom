package me.ryzeon.core.command;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.ConfigFile;
import me.ryzeon.core.utils.Utils;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PingCommand extends BaseCMD {
    @Command(name = "ping",aliases = {"ms,conexion"})
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigFile messages = Zoom.getInstance().getMessagesconfig();
        int ping = Utils.getPing(p);
        if (args.length == 0){
            p.sendMessage(Utils.format(messages.getString("ping.default"),new Object[]{ ping }));
            if (messages.getString("ping.sound") != "none" || messages.getString("ping.sound") != "NONE"){
                p.playSound(p.getLocation(), Sound.valueOf(messages.getString("ping.sound")),2F,2F);
            }
        }
        else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null){
                p.sendMessage("§cCouldn't find player.");
                return;
            }
            int pingtarget = Utils.getPing(target);
            p.sendMessage(Utils.format(messages.getString("ping.other"), new Object[]{ pingtarget }));
            if (messages.getString("ping.sound") != "none" || messages.getString("ping.sound") != "NONE"){
                p.playSound(p.getLocation(), Sound.valueOf(messages.getString("ping.sound")),2F,2F);
            }
        }
    }
}
