package club.frozed.core.command.rank;

import club.frozed.core.Zoom;
import club.frozed.core.manager.ranks.Rank;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.Clickable;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.command.Completer;
import club.frozed.core.utils.lang.Lang;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 14/09/2020 @ 21:14
 * Template by Elp1to
 */

public class RankCommand extends BaseCMD {

    @Completer(name = "rank")
    public List<String> rankCompleter(CommandArgs args) {
        if (args.length() == 1) {
            List<String> list = new ArrayList<>();
            list.add("list");
            list.add("import");
            list.add("export");
            list.add("info");
            list.add("setprefix");
            list.add("setsuffix");
            return list;
        } else if (args.length() > 1) {
            List<String> list = new ArrayList<>();
            return list;
        }
        return null;
    }

    @Command(name = "rank", permission = "core.rank.help")

    @Override
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();
        Rank rank;

        if (args.length == 0) {
            player.sendMessage(CC.MENU_BAR);
            player.sendMessage(CC.translate("&eRank help [1/2] | /rank help <page>"));
            player.sendMessage(CC.translate("&e/rank list"));
            player.sendMessage(CC.translate("&e/rank import » Load ranks from ranks.yml"));
            player.sendMessage(CC.translate("&e/rank export » Export ranks form MongoDB"));
            player.sendMessage(CC.translate("&e/rank info <rank>"));
            player.sendMessage(CC.translate("&e/rank setprefix <rank> <prefix>"));
            player.sendMessage(CC.translate("&e/rank setsetsuffix <rank> <prefix>"));
            player.sendMessage(CC.MENU_BAR);
            return;
        }
        switch (args[0]) {
            case "help":
                if (args.length < 2) return;
                if (args[1] == null) return;
                switch (args[1]) {
                    case "1":
                        player.sendMessage(CC.MENU_BAR);
                        player.sendMessage(CC.translate("&eRank help [1/2] | /rank help <page>"));
                        player.sendMessage(CC.translate("&e/rank list"));
                        player.sendMessage(CC.translate("&e/rank import » Load ranks from ranks.yml"));
                        player.sendMessage(CC.translate("&e/rank export » Export ranks form MongoDB"));
                        player.sendMessage(CC.translate("&e/rank info <rank>"));
                        player.sendMessage(CC.translate("&e/rank setprefix <rank> <prefix>"));
                        player.sendMessage(CC.translate("&e/rank setsetsuffix <rank> <prefix>"));
                        player.sendMessage(CC.MENU_BAR);
                        break;
                    case "2":
                        player.sendMessage(CC.MENU_BAR);
                        player.sendMessage(CC.translate("&eRank help [2/2] | /rank help <page>"));
                        player.sendMessage(CC.translate("&e/rank setcolor <rank> <color>"));
                        player.sendMessage(CC.translate("&e/rank setdefault <rank> <color>"));
                        player.sendMessage(CC.translate("&e/rank setbold <rank> <color>"));
                        player.sendMessage(CC.translate("&e/rank setitalic <rank> <color>"));
                        player.sendMessage(CC.translate("&e/rank setpriority <rank> <color>"));
                        player.sendMessage(CC.translate("&e/rank addperm <rank> <color>"));
                        player.sendMessage(CC.translate("&e/rank removeperm <rank> <color>"));
                        player.sendMessage(CC.MENU_BAR);
                        break;
                    default:
                        player.sendMessage(CC.translate("&e/rank help <page>"));
                        break;
                }
                break;
            case "list":
                player.sendMessage(CC.MENU_BAR);
                for (Rank ranks : Zoom.getInstance().getRankManager().getRanks()) {
                    List<String> rankInfo = new ArrayList<>();
                    rankInfo.add(CC.translate("&aPrefix&7 » " + ranks.getPrefix()));
                    rankInfo.add(CC.translate("&aSuffix&7 » " + (ranks.getSuffix().isEmpty() ? "&cNone" : ranks.getSuffix())));
                    rankInfo.add(CC.translate("&aColor&7 » " + ranks.getColor() + ranks.getColor().name()));
                    rankInfo.add(CC.translate("&aDefault&7 » " + (ranks.isDefaultRank() ? "&aYes" : "&cNo")));
                    rankInfo.add(CC.translate("&aBold&7 » " + (ranks.isBold() ? "&aYes" : "&cNo")));
                    rankInfo.add(CC.translate("&aItalic&7 » " + (ranks.isItalic() ? "&aYes" : "&cNo")));
                    rankInfo.add(CC.translate("&aTotal Permissions&7 » &f" + ranks.getPermissions().size()));
                    Clickable clickable = new Clickable(ranks.getColor() + ranks.getName() + CC.translate("&7(&a" + ranks.getPriority() + "&7)"), StringUtils.join(rankInfo, "\n"), null);
                    clickable.sendToPlayer(player);
                }
                player.sendMessage(CC.MENU_BAR);
                break;
            case "import":
                Zoom.getInstance().getRankManager().loadRanks();
                player.sendMessage(CC.translate("&aSuccessfully import ranks from ranks.yml"));
                break;
            case "export":
                Zoom.getInstance().getRankManager().saveFromMongo();
                player.sendMessage(CC.translate("&aSuccessfully import ranks from MongoDB."));
                break;
            case "info":
                if (args.length < 2) return;
                if (args[1] == null) return;
                if (!Rank.isRankExists(args[1])) {
                    player.sendMessage(CC.translate("&cThis rank don't exist"));
                    return;
                }
                rank = Rank.getRankByName(args[1]);
                player.sendMessage(CC.MENU_BAR);
                player.sendMessage(CC.translate(rank.getColor() + rank.getName() + " info."));
                player.sendMessage(CC.translate("&aPrefix&7 » " + rank.getPrefix()));
                player.sendMessage(CC.translate("&aSuffix&7 » " + (rank.getSuffix().isEmpty() ? "&cNone" : rank.getSuffix())));
                player.sendMessage(CC.translate("&aColor&7 » " + rank.getColor() + rank.getColor().name()));
                player.sendMessage(CC.translate("&aDefault&7 » " + (rank.isDefaultRank() ? "&aYes" : "&cNo")));
                player.sendMessage(CC.translate("&aBold&7 » " + (rank.isBold() ? "&aYes" : "&cNo")));
                player.sendMessage(CC.translate("&aItalic&7 » " + (rank.isItalic() ? "&aYes" : "&cNo")));
                player.sendMessage(CC.translate("&aTotal Permissions&7 » &f" + rank.getPermissions().size()));
                player.sendMessage(CC.MENU_BAR);
                break;
            case "setprefix":
                if (args.length < 3) return;
                if (args[1] == null) return;
                if (!Rank.isRankExists(args[1])) {
                    player.sendMessage(CC.translate("&cThis rank don't exist"));
                    return;
                }
                if (args[3] != null) {
                    rank = Rank.getRankByName(args[1]);
                    rank.setPrefix(args[3]);
                    player.sendMessage(CC.translate(Lang.PREFIX + "&7Successfully updated " + rank.getColor() + rank.getName() + " &7prefix to " + args[3]));
                } else {
                    player.sendMessage(CC.translate("&cThe rank prefix cannot be null!"));
                }
                break;
            case "setsuffix":
                if (args.length < 3) return;
                if (args[1] == null) return;
                if (!Rank.isRankExists(args[1])) {
                    player.sendMessage(CC.translate("&cThis rank don't exist"));
                    return;
                }
                if (args[3] != null) {
                    rank = Rank.getRankByName(args[1]);
                    rank.setSuffix(args[3]);
                    player.sendMessage(CC.translate(Lang.PREFIX + "&7Successfully updated " + rank.getColor() + rank.getName() + " &7suffix to " + args[3]));
                } else {
                    player.sendMessage(CC.translate("&cThe rank suffix cannot be null!"));
                }
                break;
            default:
                player.sendMessage(CC.MEDIUM_CHAT_BAR);
                player.sendMessage(CC.translate("&eRank help [1/2]"));
                player.sendMessage(CC.translate("&e/rank list"));
                player.sendMessage(CC.translate("&eUse /rank help <page>"));
                player.sendMessage(CC.MEDIUM_CHAT_BAR);
                break;
        }
    }
}
