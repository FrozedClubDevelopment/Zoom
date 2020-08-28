package club.frozed.zoom.command;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Completer;
import club.frozed.zoom.utils.Color;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
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
            p.sendMessage(Color.CHAT_BAR);
            p.sendMessage(Color.translate("&6&lZoom &7- &ev" + ZoomPlugin.getInstance().getDescription().getVersion()));
            p.sendMessage(Color.CHAT_BAR);
            p.sendMessage(Color.translate("&6Authors&f: &e" + ZoomPlugin.getInstance().getAuthors()));
            p.sendMessage(Color.translate("&6Description&f: &e" + ZoomPlugin.getInstance().getDescription().getDescription()));
            p.sendMessage(Color.translate("&6Website&f: &e" + ZoomPlugin.getInstance().getDescription().getWebsite()));
            p.sendMessage(Color.CHAT_BAR);
            return;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (!p.isOp()) return;
            try {
                ZoomPlugin.getInstance().reloadFile();
                p.sendMessage(Color.CHAT_BAR);
                p.sendMessage(Color.translate("&6&lZoom &7- &ev" + ZoomPlugin.getInstance().getDescription().getVersion()));
                p.sendMessage(Color.translate("&a&oSuccesfully reloaded: &7messages, database and settings"));
                p.sendMessage(Color.CHAT_BAR);
            } catch (Exception exception) {
                p.sendMessage(Color.translate("&cThere has been an error while reloading ZoomPlugin files!"));
            }
        }
    }
}
