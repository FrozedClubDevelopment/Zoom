package club.frozed.core.command.network;

import club.frozed.lib.chat.CC;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import club.frozed.core.utils.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Elb1to
 * Project: Zoom
 * Date: 08/27/2020 @ 21:12
 */
public class InstanceCommand extends BaseCommand {
    @Command(name = "instance", aliases = {"serverinstance", "checkinstance", "currentserver"}, permission = "core.network.instance", usage = "Usage: /instance <serverName>",inGameOnly = true)

    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate("&6&lZoom &7- &fServer Instance"));
        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate("&8 ► &7Server&f: &e" + Lang.SERVER_NAME));
        player.sendMessage(CC.translate("&8 ► &7Version&f: &e" + Bukkit.getServer().getVersion()));
        player.sendMessage(CC.translate("&8 ► &7Players&f: &e" + Bukkit.getServer().getOnlinePlayers().size() + "/" + Bukkit.getServer().getMaxPlayers()));
        player.sendMessage(CC.CHAT_BAR);
    }

}
