package club.frozed.core.command.rank;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.ranks.Rank;
import club.frozed.lib.chat.CC;
import club.frozed.core.utils.Utils;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import club.frozed.core.utils.time.DateUtils;
import club.frozed.lib.task.TaskUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 22/09/2020 @ 20:42
 */
public class SetRankCommand extends BaseCommand {

    @Command(name = "setrank", permission = "core.rank.setrank", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs cmd) {
        CommandSender player = cmd.getSender();
        String[] args = cmd.getArgs();

        TaskUtil.runAsync(() -> {
            if (args.length < 4) {
                player.sendMessage(CC.translate("&c/setrank <player> <rank> <duration> <reason>"));
                return;
            }
            if (!Rank.isRankExist(args[1])) {
                player.sendMessage(CC.translate("&cThat rank doesn't exists"));
                return;
            }

            Rank rankData = Rank.getRankByName(args[1]);
            String durationTime = "";
            long duration = -1L;
            if (args[2].equalsIgnoreCase("perm") || args[2].equalsIgnoreCase("permanent")) {
                durationTime = "permanent";
            } else {
                try {
                    duration = DateUtils.getDuration(args[2]);
                } catch (Exception e) {
                    player.sendMessage(CC.translate("&cThe duration isn't valid."));
                    return;
                }
            }

            String reason = Utils.buildMessage(args, 3);
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (target.isOnline()) {
                PlayerData targetData = PlayerData.getPlayerData(target.getUniqueId());
                if (targetData.hasRank(rankData)) {
                    player.sendMessage(CC.translate("&cError! &7That player already have " + rankData.getColor() + rankData.getName() + " &7rank."));
                    return;
                }
                Zoom.getInstance().getRankManager().giveRank(player, targetData, duration, durationTime.equalsIgnoreCase("permanent"), reason, rankData, "Global");
            } else {
                player.sendMessage(CC.translate("&aLoading player data....."));
                if (!PlayerData.hasData(target.getName())) {
                    player.sendMessage(CC.translate("&cThat player doesn't have data"));
                    return;
                }

                PlayerData targetData = PlayerData.loadData(target.getUniqueId());
                if (targetData != null) {
                    if (targetData.hasRank(rankData)) {
                        player.sendMessage(CC.translate("&cError! &7That player already have " + rankData.getColor() + rankData.getName() + " &7rank."));
                        targetData.removeData();
                        return;
                    }
                    Zoom.getInstance().getRankManager().giveRank(player, targetData, duration, durationTime.equalsIgnoreCase("permanent"), reason, rankData, "Global");
                    targetData.removeData();
                }
            }
        });
    }
}
