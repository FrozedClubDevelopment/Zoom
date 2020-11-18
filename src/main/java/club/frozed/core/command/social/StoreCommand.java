package club.frozed.core.command.social;

import club.frozed.core.Zoom;
import club.frozed.lib.chat.CC;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import club.frozed.core.utils.lang.Lang;
import org.bukkit.entity.Player;

public class StoreCommand extends BaseCommand {
    @Command(name = "store")

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        if (cmd.getArgs().length == 0) {
            p.sendMessage(CC.translate(Zoom.getInstance().getMessagesConfig().getConfiguration().getString("COMMANDS.SOCIAL.MESSAGES")
                    .replace("<command>", "Store")
                    .replace("<social>", Lang.STORE))
            );
        }
    }
}
