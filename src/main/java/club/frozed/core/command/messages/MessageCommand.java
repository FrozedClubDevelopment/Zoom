package club.frozed.core.command.messages;

import club.frozed.core.manager.messages.PlayerMessage;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageCommand extends BaseCommand {
    @Command(name = "message", aliases = {"whisper", "decir"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        PlayerData data = PlayerData.getPlayerData(p.getUniqueId());
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            p.sendMessage("§cUsage: /" + cmd.getLabel() + " <player> <text>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            p.sendMessage("§eThat player isn't online");
            return;
        }
        if (target == p) {
            p.sendMessage("§cYou can't message yourself");
            return;
        }
        if (data.getIgnoredPlayersList().contains(target.getName())) {
            p.sendMessage("§cYou cannot message this player because you have him on your ignore list.");
            return;
        }

//        PlayerData targetdata = PlayerData.getByUuid(target.getUniqueId());
        PlayerData targetdata = PlayerData.getPlayerData(target.getUniqueId());
        if (targetdata == null) return;
        if (targetdata.getIgnoredPlayersList().contains(p.getName())) {
            p.sendMessage("§cYou cannot message this player, because you are on his ignore list");
            return;
        }
        if (args.length < 2) {
            p.sendMessage("§cPlease specific a message");
            return;
        }
        if (!targetdata.isTogglePrivateMessages()) {
            p.sendMessage("§cThat player disabled private messages");
            return;
        }

        String text = StringUtils.join(args, ' ', 1, args.length);
        PlayerMessage playerMessage = new PlayerMessage(p, target, text, false);
        playerMessage.send();
    }
}
