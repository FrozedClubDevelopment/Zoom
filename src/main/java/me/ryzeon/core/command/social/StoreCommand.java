package me.ryzeon.core.command.social;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.lang.Lang;
import org.bukkit.entity.Player;

public class StoreCommand extends BaseCMD {
    @Command(name = "store")
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        if (cmd.getArgs().length == 0) {
            p.sendMessage(Color.translate(Zoom.getInstance().getMessagesconfig().getConfig().getString("social.messages")
                    .replace("<command>", "Store")
                    .replace("<social>", Lang.STORE)));
        }
    }
}
