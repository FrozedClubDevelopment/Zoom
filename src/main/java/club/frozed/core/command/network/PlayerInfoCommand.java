package club.frozed.core.command.network;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.lib.chat.CC;
import club.frozed.core.utils.Clickable;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfoCommand extends BaseCommand {
    @Command(name = "playerinfo", aliases = {"pinfo", "info", "getplayer"}, permission = "core.network.playerinfo", usage = "Usage: /playerinfo <serverName>")

    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&c" + command.getCommand().getUsage()));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        PlayerData playerData = PlayerData.getPlayerData(target.getName());
        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate("&b&lZoom &7- &fPlayer Info"));
        player.sendMessage(CC.translate("&8 ▸ &7Player&f: &b" + target.getName()));
        player.sendMessage(CC.translate("&8 ▸ &7Highest Rank&f: &b" + playerData.getHighestRank().getName()));
        //player.sendMessage(CC.translate("&8 ▸ &7Permissions&f: &e" + playerData.getPermissions().size()));

        Clickable showPermsList = new Clickable();
        List<String> playerPerms = new ArrayList<>();
        playerData.getPermissions().forEach(perm -> playerPerms.add(CC.translate("&f ● &b" + perm)));
        showPermsList.add(CC.translate("&8 ▸ &7Permissions&f: &b" + playerData.getPermissions().size() + " &7(&fHover&7)"), StringUtils.join(playerPerms, "\n"), null);
        showPermsList.sendToPlayer(player);

        player.sendMessage(CC.translate("&8 ▸ &7Last Server&f: &b" + playerData.getLastServer()));
        player.sendMessage(CC.translate("&8 ▸ &7Chat Channel&f: &b" + (playerData.isStaffChat() ? "Staff Chat" : "Global Chat")));
        player.sendMessage(CC.translate("&8 ▸ &7Private Messages&f: " + (playerData.isTogglePrivateMessages() ? "&aEnabled" : "&cDisabled")));
        player.sendMessage(CC.CHAT_BAR);
    }
}
