package me.ryzeon.core.command.essentials;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.config.ConfigCursor;
import org.bukkit.entity.Player;

public class FeedCommand extends BaseCMD {
    @Command(name = "feed", permission = "core.feed", aliases = {"comer", "tragar"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesconfig(), "");
        if (args.length == 0) {
            p.setFoodLevel(20);
            p.sendMessage(Color.translate(messages.getString("feed")));
        }
    }
}