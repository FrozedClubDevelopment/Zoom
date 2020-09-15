package club.frozed.core.command.rank;

import club.frozed.core.Zoom;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.command.Completer;
import org.bukkit.Bukkit;
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
            p.sendMessage(CC.M_CHAT_BAR);
            p.sendMessage(CC.translate("&eRank help [1/2]"));
            p.sendMessage(CC.translate("&e/rank list"));
            p.sendMessage(CC.translate("&e/rank import -> Load ranks from ranks.yml"));
            p.sendMessage(CC.translate("&e/rank export -> Export ranks form MongoDB"));
            p.sendMessage(CC.translate("&eUse /rank help <page>"));
            p.sendMessage(CC.M_CHAT_BAR);
            return;
        }
        switch (args[0]){
            case "list":
                break;
            case "import":
                Zoom.getInstance().getRankManager().loadRanks();
                p.sendMessage(CC.translate("&aSuccessfully import ranks from ranks.yml"));
                break;
            case "export":
                Zoom.getInstance().getRankManager().saveFromMongo();
                p.sendMessage(CC.translate("&aSuccessfully import ranks from MongoDB."));
                break;
            default:
                p.sendMessage(CC.M_CHAT_BAR);
                p.sendMessage(CC.translate("&eRank help [1/2]"));
                p.sendMessage(CC.translate("&e/rank list"));
                p.sendMessage(CC.translate("&eUse /rank help <page>"));
                p.sendMessage(CC.M_CHAT_BAR);
                break;
        }
    }
}
