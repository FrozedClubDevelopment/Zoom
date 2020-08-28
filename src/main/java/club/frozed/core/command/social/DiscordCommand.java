package club.frozed.core.command.social;

import club.frozed.core.Zoom;
import club.frozed.core.utils.Color;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.lang.Lang;
import org.bukkit.entity.Player;

public class DiscordCommand extends BaseCMD {
    @Command(name = "discord")

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();

        if (cmd.getArgs().length == 0) {
            p.sendMessage(Color.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.SOCIAL.MESSAGES")
                    .replace("<command>", "Discord")
                    .replace("<social>", Lang.DISCORD))
            );
        }
    }
}
