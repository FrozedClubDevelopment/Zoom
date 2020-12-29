package club.frozed.core.command.essentials;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.ranks.Rank;
import club.frozed.lib.chat.CC;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 29/12/2020 @ 13:00
 */

public class ListCommand extends BaseCommand {

    @Command(name = "list", aliases = {"glist", "players"}, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs commandArgs) {
        CommandSender player = commandArgs.getSender();
        List<String> rankList = Rank.getRanks().stream().map(rank -> CC.translate(rank.getColor() + rank.getName())).collect(Collectors.toList());
        List<String> players = new ArrayList<>();
        Zoom.getInstance().getServer().getOnlinePlayers().forEach(sp -> {
            PlayerData data = PlayerData.getPlayerData(sp.getUniqueId());
            if (data != null) {
                players.add(CC.translate(data.getHighestRank().getColor().toString() + data.getName()));
            }
        });
        Zoom.getInstance().getMessagesConfig().getStringList("COMMANDS.LIST.FORMAT").forEach(text -> {
            switch (text) {
                case "<rank_list>":
                    player.sendMessage(StringUtils.join(rankList, CC.translate(Zoom.getInstance().getMessagesConfig().getString("COMMANDS.LIST.RANK-SEPARATOR")) + " "));
                    break;
                case "<players_online>":
                    player.sendMessage(StringUtils.join(players, CC.translate(Zoom.getInstance().getMessagesConfig().getString("COMMANDS.LIST.PLAYER-SEPARATOR")) + " "));
                    break;
                default:
                    player.sendMessage(CC.translate(text)
                            .replace("<online_players>", String.valueOf(Zoom.getInstance().getServer().getOnlinePlayers().size()))
                            .replace("<max_players>", String.valueOf(Zoom.getInstance().getServer().getMaxPlayers()))
                    );
                    break;
            }
        });
    }
}
