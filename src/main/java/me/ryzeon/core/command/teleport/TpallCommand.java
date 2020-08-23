package me.ryzeon.core.command.teleport;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.Utils;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TpallCommand extends BaseCMD {
    @Command(name = "teleportall", permission = "core.teleport.all", aliases = {"tpall"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor message = new ConfigCursor(Zoom.getInstance().getMessagesconfig(), "teleport");
        for (Player player : Bukkit.getOnlinePlayers())
            player.teleport(p.getLocation());
        Utils.sendAllMsg(Color.translate(message.getString("tpall").replace("<player>", p.getDisplayName())));
    }
}
