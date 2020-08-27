package me.ryzeon.core.command;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.command.Completer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ZoomCommand extends BaseCMD {
    @Completer(name = "zoom", aliases = {"zoomcore"})
    public List<String> gamemodeCompleter(CommandArgs args) {
        List<String> list = new ArrayList<String>();
        if (!args.getPlayer().isOp()) {
            return null;
        }
        if (args.length() == 1) {
            list.add("reload");
            return list;
        }
        return null;
    }

    @Command(name = "zoom", aliases = {"zoomcore"})
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        if (args.length == 0) {
            p.sendMessage("§7§m------------------------");
            p.sendMessage("§6Zoom Core");
            p.sendMessage("§6Author§f: §aRyzeon");
            p.sendMessage("§6Version§f: §av" + Zoom.getInstance().getDescription().getVersion());
            p.sendMessage("§7§m------------------------");
            return;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (!p.isOp()) return;
            try {
                Zoom.getInstance().reloadFile();
                p.sendMessage("§7§m------------------------");
                p.sendMessage("§6Zoom Core");
                p.sendMessage("§aSuccesfully reload &6messages,database,settings.yml");
                p.sendMessage("§7§m------------------------");
            } catch (Exception exception) {
                p.sendMessage("§cError in reload files :(");
            }
        }
    }
}
