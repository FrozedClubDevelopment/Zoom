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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 29/12/2020 @ 13:00
 */

public class ListCommand extends BaseCommand {

    private final Comparator<PlayerData> PLAYER_DATA_COMPARATOR = Comparator.comparingInt(PlayerData::getRankPriority).reversed();

    @Command(name = "list", aliases = {"glist", "players"}, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs commandArgs) {
        CommandSender player = commandArgs.getSender();
        List<String> rankList = Rank.getRanks().stream().sorted(Comparator.comparingInt(Rank::getPriority).reversed()).map(rank -> CC.translate(rank.getColor() + rank.getName())).collect(Collectors.toList());
        List<String> players = new ArrayList<>();
        PlayerData.getPlayerData().values().stream().filter(PlayerData::isOnline).sorted(PLAYER_DATA_COMPARATOR).forEach(playerData -> {
            players.add(CC.translate(playerData.getHighestRank().getColor().toString() + playerData.getPlayer().getName()));
        });
        Zoom.getInstance().getMessagesConfig().getStringList("COMMANDS.LIST.FORMAT").forEach(text -> {
            player.sendMessage(CC.translate(text)
                    .replace("<online_players>", String.valueOf(Zoom.getInstance().getServer().getOnlinePlayers().size()))
                    .replace("<max_players>", String.valueOf(Zoom.getInstance().getServer().getMaxPlayers()))
                    .replace("<rank_list>", StringUtils.join(rankList, CC.translate(Zoom.getInstance().getMessagesConfig().getString("COMMANDS.LIST.RANK-SEPARATOR")) + " "))
                    .replace("<players_online>", StringUtils.join(players, CC.translate(Zoom.getInstance().getMessagesConfig().getString("COMMANDS.LIST.PLAYER-SEPARATOR")) + " "))
            );
        });
    }
}
