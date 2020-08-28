package club.frozed.zoom.command.essentials;

import club.frozed.zoom.utils.Color;
import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BroadcastCommand extends BaseCMD {
    @Command(name = "broadcast", permission = "core.essentials.broadcast", aliases = {"bc", "alerta"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            p.sendMessage("§eUsage /" + cmd.getLabel() + " <text>");
            return;
        }

        List<String> text = new ArrayList<>();
        Collections.addAll(text, args);
        Bukkit.broadcastMessage(Color.translate(StringUtils.join(text, " ")));
    }
}
