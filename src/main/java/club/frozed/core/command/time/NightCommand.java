package club.frozed.core.command.time;

import club.frozed.core.Zoom;
import club.frozed.lib.chat.CC;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import club.frozed.lib.config.ConfigCursor;
import org.bukkit.entity.Player;

public class NightCommand extends BaseCommand {
    @Command(name = "night", permission = "core.command.night", inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "COMMANDS");

        if (args.length == 0) {
            p.setPlayerTime(18000L, false);
            p.sendMessage(CC.translate(messages.getString("TIME-MESSAGE").replace("<timeSetting>", String.valueOf(p.getPlayerTime()))));
        }
    }
}
