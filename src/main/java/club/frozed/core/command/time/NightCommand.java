package club.frozed.zoom.command.time;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.utils.Color;
import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
import club.frozed.zoom.utils.config.ConfigCursor;
import org.bukkit.entity.Player;

public class NightCommand extends BaseCMD {
    @Command(name = "night", permission = "core.command.night", inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(ZoomPlugin.getInstance().getMessagesConfig(), "COMMANDS");

        if (args.length == 0) {
            p.setPlayerTime(18000L, false);
            p.sendMessage(Color.translate(messages.getString("TIME-MESSAGE").replace("<timeSetting>", String.valueOf(p.getPlayerTime()))));
        }
    }
}
