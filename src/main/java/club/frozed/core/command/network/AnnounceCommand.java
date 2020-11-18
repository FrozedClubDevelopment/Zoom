package club.frozed.core.command.network;

import club.frozed.core.Zoom;
import club.frozed.lib.chat.CC;
import club.frozed.core.utils.Utils;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import club.frozed.core.utils.lang.Lang;
import org.bukkit.entity.Player;

/**
 * Created by Elb1to
 * Project: Zoom
 * Date: 08/27/2020 @ 21:14
 */
public class AnnounceCommand extends BaseCommand {
    @Command(name = "announce", aliases = {"announceserver", "alertserver"}, permission = "core.network.announce")

    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Utils.globalBroadcast(player, CC.translate(Lang.PREFIX + "&e" + player.getName() + "&7 is playing on &e" + Lang.SERVER_NAME + "&7. Use &e/join " + Lang.SERVER_NAME + "&7 to join."));
    }
}
