package club.frozed.core.command;

import club.frozed.core.Zoom;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Completer;
import club.frozed.core.utils.Color;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ZoomCommand extends BaseCMD {
    @Completer(name = "core", aliases = {"zoomcore", "zoom"})

    public List<String> gamemodeCompleter(CommandArgs args) {
        List<String> list = new ArrayList<>();
        if (!args.getPlayer().isOp()) {
            return null;
        }
        if (args.length() == 1) {
            list.add("reload");
            return list;
        }
        return null;
    }

    @Command(name = "core", aliases = {"zoomcore", "zoom"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            p.sendMessage(Color.CHAT_BAR);
            p.sendMessage(Color.translate("&6&lZoom &7- &ev" + Zoom.getInstance().getDescription().getVersion()));
            p.sendMessage(Color.CHAT_BAR);
            p.sendMessage(Color.translate("&6Authors&f: &e" + Zoom.getInstance().getAuthors()));
            p.sendMessage(Color.translate("&6Description&f: &e" + Zoom.getInstance().getDescription().getDescription()));
            p.sendMessage(Color.translate("&6Website&f: &e" + Zoom.getInstance().getDescription().getWebsite()));
            p.sendMessage(Color.CHAT_BAR);
            return;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (!p.isOp()) return;
            try {
                Zoom.getInstance().reloadFile();
                p.sendMessage(Color.CHAT_BAR);
                p.sendMessage(Color.translate("&6&lZoom &7- &ev" + Zoom.getInstance().getDescription().getVersion()));
                p.sendMessage(Color.translate("&a&oSuccesfully reloaded: &7messages, database and settings"));
                p.sendMessage(Color.CHAT_BAR);
            } catch (Exception exception) {
                p.sendMessage(Color.translate("&cThere has been an error while reloading Zoom files!"));
            }
        }
    }
}
