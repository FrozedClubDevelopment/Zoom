package club.frozed.core.command;

import club.frozed.core.Zoom;
import club.frozed.lib.chat.CC;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import club.frozed.lib.commands.Completer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ZoomCommand extends BaseCommand {
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

    @Command(name = "core", aliases = {"zoomcore", "zoom"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            p.sendMessage(CC.CHAT_BAR);
            p.sendMessage(CC.translate("&b&lZoom &7- &fv" + Zoom.getInstance().getDescription().getVersion()));
            p.sendMessage(CC.CHAT_BAR);
            p.sendMessage(CC.translate("&bAuthors&f: " + Zoom.getInstance().getAuthors()));
            p.sendMessage(CC.translate("&bDescription&f: " + Zoom.getInstance().getDescription().getDescription()));
            p.sendMessage(CC.translate("&bWebsite&f: " + Zoom.getInstance().getDescription().getWebsite()));
            p.sendMessage(CC.CHAT_BAR);
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!p.isOp()) return;
            try {
                Zoom.getInstance().reloadFile();
                p.sendMessage(CC.CHAT_BAR);
                p.sendMessage(CC.translate("&b&lZoom &7- &bv" + Zoom.getInstance().getDescription().getVersion()));
                p.sendMessage(CC.translate("&a&oSuccesfully reloaded: &7messages, database and settings"));
                p.sendMessage(CC.CHAT_BAR);
            } catch (Exception exception) {
                p.sendMessage(CC.translate("&cThere has been an error while reloading Zoom files!"));
            }
        }
    }
}
