package club.frozed.zoom.command.essentials;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.utils.Color;
import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
import club.frozed.zoom.utils.config.ConfigCursor;
import org.bukkit.entity.Player;

public class FeedCommand extends BaseCMD {
    @Command(name = "feed", permission = "core.essentials.feed", aliases = {"comer", "tragar"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(ZoomPlugin.getInstance().getMessagesConfig(), "COMMANDS");

        if (args.length == 0) {
            p.setFoodLevel(20);
            p.sendMessage(Color.translate(messages.getString("FEED-MESSAGE")));
        }
    }
}