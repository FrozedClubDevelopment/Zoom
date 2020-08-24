package me.ryzeon.core.command.essentials;

import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BroadcastCommand extends BaseCMD {
    @Command(name = "broadcast", permission = "core.broadcast", aliases = {"bc", "alerta"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        if (args.length == 0) {
            p.sendMessage("§eUsage /" + cmd.getLabel() + " <text>");
            return;
        }
        List<String> text = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            text.add(args[i]);
        }
        Bukkit.broadcastMessage(Color.translate(StringUtils.join(text, " ")));
    }
}
