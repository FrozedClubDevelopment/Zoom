package club.frozed.core.command.rank.grant;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.menu.grant.grants.GrantsMenu;
import club.frozed.lib.chat.CC;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 24/09/2020 @ 21:29
 */

public class GrantsCommand extends BaseCommand {
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
            PlayerData targetData = PlayerData.getPlayerData(target.getName());
            (new GrantsMenu(targetData)).openMenu(player);
        } else {
            player.sendMessage(CC.translate("&eLoading player data....."));
            PlayerData targetData = PlayerData.loadData(target.getUniqueId()   );
            if (targetData == null){
                player.sendMessage(CC.translate("&cThat player doesn't have data"));
                return;
            }
            (new GrantsMenu(targetData)).openMenu(player);
        }
    }
}
