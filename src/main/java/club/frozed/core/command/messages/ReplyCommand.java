package club.frozed.zoom.command.messages;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.manager.messages.PlayerMessage;
import club.frozed.zoom.utils.TaskUtil;
import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
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
            UUID lastReply = ZoomPlugin.getInstance().getMessageManager().getLastReplied().get(p.getUniqueId());
            Player target = Bukkit.getPlayer(lastReply);
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
