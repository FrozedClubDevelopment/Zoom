package me.ryzeon.core.command.messages;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.manager.messages.PlayerMessage;
import me.ryzeon.core.utils.TaskUtil;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReplyCommand extends BaseCMD {
    @Command(name = "reply", aliases = {"r"})
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        if (args.length == 0) {
            p.sendMessage("§eUsage /" + cmd.getLabel() + " <text>");
            return;
        }
        TaskUtil.runAsync(() -> {
            UUID lastpreply = Zoom.getInstance().getMessageManager().getLastReplied().get(p.getUniqueId());
            Player target = Bukkit.getPlayer(lastpreply);
            if (target == null) {
                p.sendMessage("§cThere is no player to reply to");
                return;
            }
            String text = StringUtils.join(args, ' ', 0, args.length);
            PlayerMessage playerMessage = new PlayerMessage(p, target, text, true);
            playerMessage.send();
        });
    }
}
