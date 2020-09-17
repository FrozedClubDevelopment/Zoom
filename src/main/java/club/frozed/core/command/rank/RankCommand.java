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
import org.bukkit.ChatColor;
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
            player.sendMessage(CC.translate("&e/rank setsuffix <rank> <prefix>"));
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
                        player.sendMessage(CC.translate("&e/rank setsuffix <rank> <suffix>"));
                        player.sendMessage(CC.MENU_BAR);
                        break;
                    case "2":
                        player.sendMessage(CC.MENU_BAR);
                        player.sendMessage(CC.translate("&eRank help [2/2] | /rank help <page>"));
                        player.sendMessage(CC.translate("&e/rank setcolor <rank> <color>"));
                        player.sendMessage(CC.translate("&e/rank setdefault <rank>"));
                        player.sendMessage(CC.translate("&e/rank setbold <rank>"));
                        player.sendMessage(CC.translate("&e/rank setitalic <rank>"));
                        player.sendMessage(CC.translate("&e/rank setpriority <rank> <priority>"));
                        player.sendMessage(CC.translate("&e/rank addperm <rank> <permission>"));
                        player.sendMessage(CC.translate("&e/rank removeperm <rank> <permission>"));
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
                Zoom.getInstance().getRankManager().loadRanksFromConfig();
                player.sendMessage(CC.translate("&aSuccessfully import ranks from ranks.yml"));
                break;
            case "export":
                Zoom.getInstance().getRankManager().saveFromMongo();
                player.sendMessage(CC.translate("&aSuccessfully export ranks from MongoDB."));
                break;
            case "info":
                if (rankGetterWithTwoArgs(player, args)) return;
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
                if (rankGetterWithThreeArgs(player, args)) return;
                if (args[3] != null) {
                    rank = Rank.getRankByName(args[1]);
                    rank.setPrefix(args[3]);
                    player.sendMessage(CC.translate(Lang.PREFIX + "&7Successfully updated " + rank.getColor() + rank.getName() + " &7prefix to " + args[3]));
                } else {
                    player.sendMessage(CC.translate("&cThe rank prefix cannot be null!"));
                }
                break;
            case "setsuffix":
                if (rankGetterWithThreeArgs(player, args)) return;
                if (args[3] != null) {
                    rank = Rank.getRankByName(args[1]);
                    rank.setSuffix(args[3]);
                    player.sendMessage(CC.translate(Lang.PREFIX + "&7Successfully updated " + rank.getColor() + rank.getName() + " &7suffix to " + args[3]));
                } else {
                    player.sendMessage(CC.translate("&cThe rank suffix cannot be null!"));
                }
                break;
            case "setcolor":
                if (rankGetterWithThreeArgs(player, args)) return;
                if (args[3] != null) {
                    rank = Rank.getRankByName(args[1]);
                    rank.setColor(ChatColor.getByChar(args[3]));
                    player.sendMessage(CC.translate(Lang.PREFIX + "&7Successfully updated " + rank.getName() + " &7color from " + rank.getColor().toString() + " &7to &a" + args[3]));
                } else {
                    player.sendMessage(CC.translate("&cThe rank priority cannot be lower than 0!"));
                }
                break;
            case "setpriority":
                if (rankGetterWithThreeArgs(player, args)) return;
                if (args[3] != null) {
                    rank = Rank.getRankByName(args[1]);
                    rank.setPriority(Integer.parseInt(args[3]));
                    player.sendMessage(CC.translate(Lang.PREFIX + "&7Successfully updated " + rank.getColor() + rank.getName() + " &7priority from &c" + rank.getPriority() + " &7to &a" + args[3]));
                } else {
                    player.sendMessage(CC.translate("&cThe rank priority cannot be lower than 0!"));
                }
                break;
            case "setdefault":
                if (rankGetterWithTwoArgs(player, args)) return;
                if (args[2] != null) {
                    rank = Rank.getRankByName(args[1]);
                    if (!rank.isDefaultRank()) {
                        rank.setDefaultRank(Boolean.parseBoolean(args[2]));
                        player.sendMessage(CC.translate(Lang.PREFIX + "&aSuccess! " + rank.getColor() + rank.getName() + "&7 will now be the default rank!"));
                    } else {
                        player.sendMessage(CC.translate("&cOnly 1 rank can be default!"));
                    }
                } else {
                    player.sendMessage(CC.translate("&cThe specified rank doesn't exist!"));
                }
                break;
            case "setbold":
                if (rankGetterWithTwoArgs(player, args)) return;
                if (args[2] != null) {
                    rank = Rank.getRankByName(args[1]);
                    rank.setBold(Boolean.parseBoolean(args[2]));
                    player.sendMessage(CC.translate(Lang.PREFIX + "&aSuccess! &7Now " + rank.getColor() + rank.getName() + (rank.isItalic() ? "&7 is now bold!" : "&7 is no longer bold.")));
                } else {
                    player.sendMessage(CC.translate("&cThe specified rank doesn't exist!"));
                }
                break;
            case "setitalic":
                if (rankGetterWithTwoArgs(player, args)) return;
                if (args[2] != null) {
                    rank = Rank.getRankByName(args[1]);
                    rank.setItalic(Boolean.parseBoolean(args[2]));
                    player.sendMessage(CC.translate(Lang.PREFIX + "&aSuccess! &7Now " + rank.getColor() + rank.getName() + (rank.isItalic() ? "&7 is now italic!" : "&7 is no longer italic.")));
                } else {
                    player.sendMessage(CC.translate("&cThe specified rank doesn't exist!"));
                }
                break;
            case "addperm":
                if (rankGetterWithTwoArgs(player, args)) return;
                if (args[2] != null) {
                    rank = Rank.getRankByName(args[1]);
                    if (!rank.getPermissions().contains(args[2])){
                        rank.getPermissions().add(args[2]);
                    }
                    player.sendMessage(CC.translate(Lang.PREFIX + "&Success! &7Added " + args[2] + " permission to rank " + rank.getColor() + rank.getName()));
                } else {
                    player.sendMessage(CC.translate("&cThe specified rank doesn't exist!"));
                }
                break;
            case "removeperm":
                if (rankGetterWithTwoArgs(player, args)) return;
                if (args[2] != null) {
                    rank = Rank.getRankByName(args[1]);
                    if (rank.getPermissions().contains(args[2])){
                        rank.getPermissions().remove(args[2]);
                    }
                    player.sendMessage(CC.translate(Lang.PREFIX + "&Success! &7Remove " + args[2] + " permission to rank " + rank.getColor() + rank.getName()));
                } else {
                    player.sendMessage(CC.translate("&cThe specified rank doesn't exist!"));
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

    private boolean rankGetterWithThreeArgs(Player player, String[] args) {
        if (args.length < 3) return true;
        if (args[1] == null) return true;
        if (!Rank.isRankExists(args[1])) {
            player.sendMessage(CC.translate("&cThis rank don't exist"));
            return true;
        }
        return false;
    }

    private boolean rankGetterWithTwoArgs(Player player, String[] args) {
        if (args.length < 2) return true;
        if (args[1] == null) return true;
        if (!Rank.isRankExists(args[1])) {
            player.sendMessage(CC.translate("&cThis rank don't exist"));
            return true;
        }
        return false;
    }
}
