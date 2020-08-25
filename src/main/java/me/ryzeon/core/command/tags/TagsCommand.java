package me.ryzeon.core.command.tags;

import me.ryzeon.core.menu.tags.TagsMenu;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import org.bukkit.entity.Player;

public class TagsCommand extends BaseCMD {
    @Command(name = "tags", permission = "core.tags", aliases = {"tag", "prefix"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        if (args.length == 0) {
            new TagsMenu().open(p);
        }
    }
}