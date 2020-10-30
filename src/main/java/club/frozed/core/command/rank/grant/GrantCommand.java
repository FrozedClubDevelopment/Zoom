package club.frozed.core.command.rank.grant;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.menu.grant.procedure.GrantMenu;
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
 * Date: 24/09/2020 @ 09:07
 */

public class GrantCommand extends BaseCMD {

    @Command(name = "grant",permission = "core.rank.grant")

    @Override
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();
        if (args.length == 0){
            player.sendMessage(CC.translate("&e/"+cmd.getLabel() + "<player>"));
            return;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target.isOnline()) {
            PlayerData targetData = PlayerData.getPlayerData(Bukkit.getPlayer(args[0]).getUniqueId());
            (new GrantMenu(targetData)).openMenu(player);
        } else {
            player.sendMessage(CC.translate("&eLoading player data....."));
            PlayerData targetData = PlayerData.loadData(target.getUniqueId());
            (new GrantMenu(targetData)).openMenu(player);
        }
    }
}
