package club.frozed.core.command.punishment;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.menu.punishments.menus.PunishmentsMenu;
import club.frozed.lib.chat.CC;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 1/11/2020 @ 13:28
 */

public class CheckPunishmentsCommand extends BaseCommand {

    @Command(name = "check", aliases = {"checkplayerpunishemnts", "punishments", "c", "history"}, permission = "core.punishments.check")
    @Override
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0){
            player.sendMessage(CC.translate("&e/" + cmd.getLabel() + " <player>"));
            return;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        PlayerData data;
        if (offlinePlayer.isOnline()) {
            data = PlayerData.getPlayerData(offlinePlayer.getUniqueId());
        } else {
            player.sendMessage(CC.translate("&eLoading player data....."));
            data = PlayerData.loadData(offlinePlayer.getUniqueId());
            if (data == null){
                player.sendMessage(CC.translate("&cError! &7That player doesn't have data"));
                return;
            }
            data.findAlts();
        }
        new PunishmentsMenu(data).openMenu(player);
    }
}
