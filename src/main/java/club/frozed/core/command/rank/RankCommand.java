package club.frozed.core.command.rank;

import club.frozed.core.Zoom;
import club.frozed.core.manager.ranks.Rank;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.Clickable;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.command.Completer;
import com.sun.deploy.security.AbstractBrowserAuthenticator;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Sound;
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
            List<String> list = new ArrayList<String>();
            list.add("list");
            list.add("import");
            list.add("export");
            list.add("info");
            list.add("setprefix");
            list.add("setsuffix");
            return list;
        } else if (args.length() > 1) {
            List<String> list = new ArrayList<String>();
            return list;
        }
        return null;
    }

    @Command(name = "rank",permission = "core.rank.help" ,inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0){
            p.sendMessage(CC.MENU_BAR);
            p.sendMessage(CC.translate("&eRank help [1/2] | /rank help <page>"));
            p.sendMessage(CC.translate("&e/rank list"));
            p.sendMessage(CC.translate("&e/rank import » Load ranks from ranks.yml"));
            p.sendMessage(CC.translate("&e/rank export » Export ranks form MongoDB"));
            p.sendMessage(CC.translate("&e/rank info <rank>"));
            p.sendMessage(CC.translate("&e/rank setprefix <rank> <prefix>"));
            p.sendMessage(CC.translate("&e/rank setsetsuffix <rank> <prefix>"));
            p.sendMessage(CC.MENU_BAR);
            return;
        }
        switch (args[0]){
            case "help":
                if (args.length < 2) return;
                if (args[1] == null) return;
                switch (args[1]){
                    case "1":
                        p.sendMessage(CC.MENU_BAR);
                        p.sendMessage(CC.translate("&eRank help [1/2] | /rank help <page>"));
                        p.sendMessage(CC.translate("&e/rank list"));
                        p.sendMessage(CC.translate("&e/rank import » Load ranks from ranks.yml"));
                        p.sendMessage(CC.translate("&e/rank export » Export ranks form MongoDB"));
                        p.sendMessage(CC.translate("&e/rank info <rank>"));
                        p.sendMessage(CC.translate("&e/rank setprefix <rank> <prefix>"));
                        p.sendMessage(CC.translate("&e/rank setsetsuffix <rank> <prefix>"));
                        p.sendMessage(CC.MENU_BAR);
                        break;
                    case "2":
                        p.sendMessage(CC.MENU_BAR);
                        p.sendMessage(CC.translate("&eRank help [2/2] | /rank help <page>"));
                        p.sendMessage(CC.translate("&e/rank setcolor <rank> <color>"));
                        p.sendMessage(CC.translate("&e/rank setdefault <rank> <color>"));
                        p.sendMessage(CC.translate("&e/rank setbold <rank> <color>"));
                        p.sendMessage(CC.translate("&e/rank setitalic <rank> <color>"));
                        p.sendMessage(CC.translate("&e/rank setpriority <rank> <color>"));
                        p.sendMessage(CC.translate("&e/rank addperm <rank> <color>"));
                        p.sendMessage(CC.translate("&e/rank removeperm <rank> <color>"));
                        p.sendMessage(CC.MENU_BAR);
                        break;
                    default:
                        p.sendMessage(CC.translate("&e/rank help <page>"));
                        break;
                }
                break;
            case "list":
                p.sendMessage(CC.MENU_BAR);
                for (Rank rank : Zoom.getInstance().getRankManager().getRanks()){
                    List<String> rankInfo = new ArrayList<>();
                    rankInfo.add(CC.translate("&aPrefix&7 » " + rank.getPrefix()));
                    rankInfo.add(CC.translate("&aSuffix&7 » " + (rank.getSuffix().isEmpty() ? "&cNone" : rank.getSuffix())));
                    rankInfo.add(CC.translate("&aColor&7 » " + rank.getColor() + rank.getColor().name()));
                    rankInfo.add(CC.translate("&aDefault&7 » " + (rank.isDefaultRank() ? "&aYes" : "&cNo")));
                    rankInfo.add(CC.translate("&aBold&7 » " + (rank.isBold() ? "&aYes" : "&cNo")));
                    rankInfo.add(CC.translate("&aItalic&7 » " + (rank.isItalic() ? "&aYes" : "&cNo")));
                    rankInfo.add(CC.translate("&aTotal Permissions&7 » &f" + rank.getPermissions().size()));
                    Clickable clickable = new Clickable(rank.getColor() + rank.getName() + CC.translate("&7(&a" + rank.getPriority() + "&7)"), StringUtils.join(rankInfo,"\n"), null);
                    clickable.sendToPlayer(p);
                }
                p.sendMessage(CC.MENU_BAR);
                break;
            case "import":
                Zoom.getInstance().getRankManager().loadRanks();
                p.sendMessage(CC.translate("&aSuccessfully import ranks from ranks.yml"));
                break;
            case "export":
                Zoom.getInstance().getRankManager().saveFromMongo();
                p.sendMessage(CC.translate("&aSuccessfully import ranks from MongoDB."));
                break;
            case "info":
                if (args.length < 2) return;
                if (args[1] == null)  return;
                if (!Rank.isRankExists(args[1])) {
                    p.sendMessage(CC.translate("&cThis rank don't exist"));
                    return;
                }
                Rank rank = Rank.getRankByName(args[1]);
                p.sendMessage(CC.MENU_BAR);
                p.sendMessage(CC.translate(rank.getColor() + rank.getName() + " info."));
                p.sendMessage(CC.translate("&aPrefix&7 » " + rank.getPrefix()));
                p.sendMessage(CC.translate("&aSuffix&7 » " + (rank.getSuffix().isEmpty() ? "&cNone" : rank.getSuffix())));
                p.sendMessage(CC.translate("&aColor&7 » " + rank.getColor() + rank.getColor().name()));
                p.sendMessage(CC.translate("&aDefault&7 » " + (rank.isDefaultRank() ? "&aYes" : "&cNo")));
                p.sendMessage(CC.translate("&aBold&7 » " + (rank.isBold() ? "&aYes" : "&cNo")));
                p.sendMessage(CC.translate("&aItalic&7 » " + (rank.isItalic() ? "&aYes" : "&cNo")));
                p.sendMessage(CC.translate("&aTotal Permissions&7 » &f" + rank.getPermissions().size()));
                p.sendMessage(CC.MENU_BAR);
                break;
            default:
                p.sendMessage(CC.MEDIUM_CHAT_BAR);
                p.sendMessage(CC.translate("&eRank help [1/2]"));
                p.sendMessage(CC.translate("&e/rank list"));
                p.sendMessage(CC.translate("&eUse /rank help <page>"));
                p.sendMessage(CC.MEDIUM_CHAT_BAR);
                break;
        }
    }
}
