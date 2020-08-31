package club.frozed.core.command.essentials;

import club.frozed.core.Zoom;
import club.frozed.core.utils.Color;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BroadcastCommand extends BaseCMD {
    @Command(name = "broadcast", permission = "core.essentials.broadcast", aliases = {"bc", "alerta"})

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
        Bukkit.broadcastMessage(Color.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.BROADCAST")
                .replace("<text>", StringUtils.join(text, " "))));
    }
}
