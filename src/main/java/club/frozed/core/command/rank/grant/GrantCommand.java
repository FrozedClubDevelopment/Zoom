package club.frozed.core.command.rank.grant;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.menu.grant.procedure.GrantMenu;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import org.bukkit.Bukkit;
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
        if (args.length == 0) return;
        PlayerData playerData = PlayerData.getByUuid(Bukkit.getPlayer(args[0]).getUniqueId());
        new GrantMenu(playerData).open(player);
    }
}
