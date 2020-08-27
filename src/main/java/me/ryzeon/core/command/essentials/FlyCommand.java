package me.ryzeon.core.command.essentials;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import org.bukkit.entity.Player;

public class FlyCommand extends BaseCMD {
    @Command(name = "fly", permission = "core.essentials.fly", aliases = {"flying"})
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        if (args.length == 0) {
            String status = !p.isFlying() ? "§aenabled" : "§cdisabled";
            p.setAllowFlight(!p.getAllowFlight());
            p.setFlying(!p.isFlying());
            p.sendMessage(Color.translate(Zoom.getInstance().getMessagesconfig().getConfig().getString("fly").replace("<status>", status)));
        }
    }
}
