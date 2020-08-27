package me.ryzeon.core.command.messages;

import me.ryzeon.core.manager.player.PlayerData;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.command.Completer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class IgnoreCommand extends BaseCMD {
    @Completer(name = "ignore", aliases = "ignorelist")
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

    @Command(name = "ignore", aliases = {"ignorelist"})
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        PlayerData data = PlayerData.getByUuid(p.getUniqueId());
        if (args.length == 0) {
            p.sendMessage(Color.LINE);
            p.sendMessage("§e/ignore list");
            p.sendMessage("§e/ignore add");
            p.sendMessage("§e/ignore remove");
            p.sendMessage(Color.LINE);
            return;
        }
        switch (args[0]) {
            case "list": {
                List<String> ignore = new ArrayList<>();
                if (data.getIgnorelist().isEmpty()) {
                    ignore.add("§eNo players found.");
                } else {
                    data.getIgnorelist().forEach(name -> ignore.add("§7» §f" + name));
                }
                p.sendMessage(Color.LINE);
                p.sendMessage("§eIgnore List");
                p.sendMessage("");
                p.sendMessage(StringUtils.join(ignore, "\n"));
                p.sendMessage(Color.LINE);
            }
            break;
            case "add": {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    p.sendMessage("§cThat player isn't online");
                    return;
                }
                if (target.getName() == p.getName()) {
                    p.sendMessage("§cYou can't ignore yourself");
                    return;
                }
                for (String name : data.getIgnorelist()) {
                    if (target.getName() == name) {
                        p.sendMessage("§cThat player is already in ignored list");
                        return;
                    }
                }
                data.getIgnorelist().add(target.getName());
                p.sendMessage("§aYou added §f" + target.getName() + " §eto your ignored list.");
            }
            break;
            case "remove": {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    p.sendMessage("§cThat player isn't online");
                    return;
                }
                if (target.getName() == p.getName()) {
                    p.sendMessage("§cYou can't ignore yourself");
                    return;
                }
                data.getIgnorelist().remove(target.getName());
                p.sendMessage("§aYou remove §f" + target.getName() + " §eto your ignored list.");
            }
            break;
        }
    }
}
