package club.frozed.core.command.tags;

import club.frozed.core.Zoom;
import club.frozed.core.menu.tags.TagsMenu;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.command.Completer;
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
            new TagsMenu().openMenu(p);
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!p.isOp()) return;
            try {
                Zoom.getInstance().getTagManager().deleteTags();
                Zoom.getInstance().reloadTags();
                Zoom.getInstance().getTagManager().registerTags();
                p.sendMessage("§aSuccessfully reloaded tags");
            } catch (Exception exception) {
                p.sendMessage("§cAn error occurred while reloading the tags!");
            }
        }
    }
}