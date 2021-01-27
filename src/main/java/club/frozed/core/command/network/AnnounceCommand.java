package club.frozed.core.command.network;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.lang.Lang;
import club.frozed.lib.chat.CC;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
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
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());

        Utils.globalBroadcast(player, CC.translate(Zoom.getInstance().getSettingsConfig().getString("SETTINGS.SERVER-ANNOUNCE"))
                .replace("<name>", player.getName())
                .replace("<rank>", playerData.getHighestRank().getPrefix())
                .replace("<server_name>", Lang.SERVER_NAME)
        );
    }
}
