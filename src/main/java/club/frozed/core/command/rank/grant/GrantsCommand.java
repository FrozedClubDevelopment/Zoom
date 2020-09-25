package club.frozed.core.command.rank.grant;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.PlayerOfflineData;
import club.frozed.core.menu.grant.GrantsMenu;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 24/09/2020 @ 21:29
 */

public class GrantsCommand extends BaseCMD {
    @Command(name = "grants",permission = "core.rank.grants")
    @Override
    public void onCommand(CommandArgs cmd) {
        String[] args = cmd.getArgs();
        Player player = cmd.getPlayer();
        if (args.length == 0) {
            player.sendMessage(CC.translate("&e/grants <player>"));
            return;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target.isOnline()) {
            PlayerData targetData = PlayerData.getByName(target.getName());
            (new GrantsMenu(targetData)).open(player);
        } else {
            player.sendMessage(CC.translate("&eLoading player data....."));
            if (!PlayerOfflineData.hasData(target.getName())){
                player.sendMessage(CC.translate("&cThat player doesn't have data"));
                return;
            }
            PlayerData targetData = PlayerOfflineData.loadData(target.getName());
            (new GrantsMenu(targetData)).open(player);
        }
    }
}
