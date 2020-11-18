package club.frozed.core.command.essentials;

import club.frozed.core.Zoom;
import club.frozed.lib.chat.CC;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import club.frozed.lib.config.ConfigCursor;
import org.bukkit.entity.Player;

public class FeedCommand extends BaseCommand {
    @Command(name = "feed", permission = "core.essentials.feed", aliases = {"comer", "tragar"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "COMMANDS");

        if (args.length == 0) {
            p.setFoodLevel(20);
            p.sendMessage(CC.translate(messages.getString("FEED-MESSAGE")));
        }
    }
}