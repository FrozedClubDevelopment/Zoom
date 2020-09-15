package club.frozed.core.command.social;

import club.frozed.core.Zoom;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.lang.Lang;
import org.bukkit.entity.Player;

public class TeamSpeakCommand extends BaseCMD {
    @Command(name = "teamspeak", aliases = {"ts", "ts3"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        if (cmd.getArgs().length == 0) {
            p.sendMessage(CC.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.SOCIAL.MESSAGES")
                    .replace("<command>", "TeamSpeak")
                    .replace("<social>", Lang.TS))
            );
        }
    }
}
