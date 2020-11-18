package club.frozed.core.command.essentials;

import club.frozed.core.Zoom;
import club.frozed.lib.chat.CC;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BroadcastCommand extends BaseCommand {
    @Command(name = "broadcast", permission = "core.essentials.broadcast", aliases = {"bc", "alerta"}, inGameOnly = false)

    @Override
    public void onCommand(CommandArgs cmd) {
        CommandSender p = cmd.getSender();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            p.sendMessage("§cUsage: /" + cmd.getLabel() + " <text>");
            return;
        }

        List<String> text = new ArrayList<>();
        Collections.addAll(text, args);
        Bukkit.broadcastMessage(CC.translate(Zoom.getInstance().getMessagesConfig().getConfiguration().getString("COMMANDS.BROADCAST")
                .replace("<text>", StringUtils.join(text, " "))));
    }
}
