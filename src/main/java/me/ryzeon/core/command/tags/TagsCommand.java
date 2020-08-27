package me.ryzeon.core.command.tags;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.menu.tags.TagsMenu;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.command.Completer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TagsCommand extends BaseCMD {
    @Completer(name = "tags", aliases = {"tag", "prefix"})
    public List<String> gamemodeCompleter(CommandArgs args) {
        if (args.length() == 1) {
            List<String> list = new ArrayList<String>();
            if (args.getPlayer().isOp()) {
                list.add("reload");
                return list;
            }
        }
        return null;
    }

    @Command(name = "tags", permission = "core.tags", aliases = {"tag", "prefix"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        if (args.length == 0) {
            new TagsMenu().open(p);
            return;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (!p.isOp()) return;
            try {
                Zoom.getInstance().getTagManager().deleteTags();
                Zoom.getInstance().reloadTags();
                Zoom.getInstance().getTagManager().registerTags();
                p.sendMessage("§aSuccessfully  reload tags");
            } catch (Exception exception) {
                p.sendMessage("§cError in try to reload tags");
            }
        }
    }
}