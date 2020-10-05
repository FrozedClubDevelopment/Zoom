package club.frozed.core.command.network;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.Clickable;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfoCommand extends BaseCMD {
    @Command(name = "playerinfo", aliases = {"pinfo", "info", "getplayer"}, permission = "core.network.playerinfo", usage = "Usage: /playerinfo <serverName>")

    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&c" + command.getCommand().getUsage()));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        PlayerData playerData = PlayerData.getByName(target.getName());
        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate("&6&lZoom &7- &fPlayer Info"));
        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate("&fTarget Player&7: &6" + target.getName()));
        player.sendMessage(CC.translate("&8 ► &7Highest Rank&f: &e" + playerData.getHighestRank().getName()));
        //player.sendMessage(CC.translate("&8 ► &7Permissions&f: &e" + playerData.getPermissions().size()));

        Clickable showPermsList = new Clickable();
        List<String> playerPerms = new ArrayList<>();
        playerData.getPermissions().forEach(perm -> playerPerms.add(CC.translate("&f ● &6" + perm)));
        showPermsList.add(CC.translate("&8 ► &7Permissions&f: &e" + playerData.getPermissions().size() + " &7(&fHover&7)"), StringUtils.join(playerPerms, "\n"), null);
        showPermsList.sendToPlayer(player);

        player.sendMessage(CC.translate("&8 ► &7Last Server&f: &e" + playerData.getLastServer()));
        player.sendMessage(CC.translate("&8 ► &7Chat Channel&f: &e" + (playerData.isStaffChat() ? "Staff Chat" : "Global Chat")));
        player.sendMessage(CC.translate("&8 ► &7Private Messages&f: " + (playerData.isTogglePrivateMessages() ? "&aEnabled" : "&cDisabled")));
        player.sendMessage(CC.CHAT_BAR);
    }
}
