package club.frozed.core.command.messages;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.command.Completer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class IgnoreCommand extends BaseCMD {
    @Completer(name = "ignore", aliases = "ignoredPlayersList")

    public List<String> IgnoreCompleter(CommandArgs args) {
        List<String> list = new ArrayList<String>();
        if (args.length() == 1) {
            list.add("add");
            list.add("remove");
            list.add("list");
        }
        if (args.length() == 2) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                list.add(p.getName());
            }
        }
        return list;
    }

    @Command(name = "ignore", aliases = {"ignoredPlayersList"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        PlayerData data = PlayerData.getPlayerData(p.getUniqueId());

        if (args.length == 0) {
            p.sendMessage(CC.CHAT_BAR);
            p.sendMessage("§e/ignore list");
            p.sendMessage("§e/ignore add");
            p.sendMessage("§e/ignore remove");
            p.sendMessage(CC.CHAT_BAR);
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        switch (args[0]) {
            case "list":
                List<String> ignore = new ArrayList<>();
                if (data.getIgnoredPlayersList().isEmpty()) {
                    ignore.add("§eNo players found.");
                } else {
                    data.getIgnoredPlayersList().forEach(name -> ignore.add("§7» §f" + name));
                }
                p.sendMessage(CC.CHAT_BAR);
                p.sendMessage("§eIgnore List");
                p.sendMessage("");
                p.sendMessage(StringUtils.join(ignore, "\n"));
                p.sendMessage(CC.CHAT_BAR);
                break;
            case "add":
                if (target == null) {
                    p.sendMessage("§cThat player isn't online");
                    return;
                }
                if (target.getName() == p.getName()) {
                    p.sendMessage("§cYou can't ignore yourself");
                    return;
                }
                for (String name : data.getIgnoredPlayersList()) {
                    if (target.getName() == name) {
                        p.sendMessage("§cThat player is already in ignored list");
                        return;
                    }
                }
                data.getIgnoredPlayersList().add(target.getName());
                p.sendMessage("§aYou added §f" + target.getName() + " §eto your ignored list.");
                break;
            case "remove":
                if (target == null) {
                    p.sendMessage("§cThat player isn't online");
                    return;
                }
                if (target.getName() == p.getName()) {
                    p.sendMessage("§cYou can't ignore yourself");
                    return;
                }
                data.getIgnoredPlayersList().remove(target.getName());
                p.sendMessage("§aYou remove §f" + target.getName() + " §eto your ignored list.");
                break;
        }
    }
}
