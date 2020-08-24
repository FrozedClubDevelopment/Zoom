package me.ryzeon.core.command.social;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.lang.Lang;
import org.bukkit.entity.Player;

public class TeamSpeakCommand extends BaseCMD {
    @Command(name = "teamspeak", aliases = {"ts", "ts3"})
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        if (cmd.getArgs().length == 0) {
            p.sendMessage(Color.translate(Zoom.getInstance().getMessagesconfig().getConfig().getString("social.messages")
                    .replace("<command>", "TeamSpeak")
                    .replace("<social>", Lang.TS)));
        }
    }
}
