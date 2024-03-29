package club.frozed.core.command.punishment;

import club.frozed.core.Zoom;
import club.frozed.core.manager.database.redis.payload.Payload;
import club.frozed.core.manager.database.redis.payload.RedisMessage;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.punishments.Punishment;
import club.frozed.core.manager.player.punishments.PunishmentType;
import club.frozed.lib.chat.CC;
import club.frozed.core.utils.Utils;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import club.frozed.core.utils.punishment.PunishmentUtil;
import club.frozed.lib.task.TaskUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 1/11/2020 @ 12:54
 */

public class UnblacklistCommand extends BaseCommand {

    @Command(name = "unblacklist", aliases = {"unipban"}, inGameOnly = false, permission = "core.punishments.unblacklist")
    @Override
    public void onCommand(CommandArgs cmd) {
        CommandSender commandSender = cmd.getSender();
        String[] args = cmd.getArgs();

        TaskUtil.runAsync(() -> {
            if (args.length == 0) {
                commandSender.sendMessage(CC.translate("&c/" + cmd.getLabel() + " <player> [reason] [-s]"));
                return;
            }
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            PlayerData data;
            if (offlinePlayer.isOnline()) {
                data = PlayerData.getPlayerData(offlinePlayer.getUniqueId());
            } else {
                commandSender.sendMessage(CC.translate("&aLoading player data..."));
                data = PlayerData.loadData(offlinePlayer.getUniqueId());
                if (data == null) {
                    commandSender.sendMessage(CC.translate("&cError! &7That player doesn't have data"));
                    return;
                }
            }
            Punishment punishment = data.getActivePunishment(PunishmentType.BLACKLIST);
            if (punishment == null) {
                commandSender.sendMessage(CC.translate("&cError! &7" + data.getName() + " isn't blacklisted."));
                return;
            }
            String reason;
            boolean silent;
            if (args.length == 1) {
                reason = "No Reason Provided";
                silent = false;
            } else {
                reason = PunishmentUtil.reasonBuilder(args, 1);
                silent = reason.contains("-S") || reason.contains("-SILENT") || reason.contains("-s") || reason.contains("-silent");
                reason = PunishmentUtil.getReasonAndRemoveSilent(reason);
            }
            punishment.setPardonedAt(System.currentTimeMillis());
            punishment.setPardonedReason(reason);
            punishment.setPardoned(true);
            if (commandSender instanceof Player) {
                punishment.setPardonedBy(((Player) commandSender).getUniqueId());
            }
            data.saveData();

            if (Zoom.getInstance().getRedisManager().isActive()) {
                String json = new RedisMessage(Payload.PUNISHMENTS_ADDED)
                        .setParam("PUNISHMENT", PunishmentUtil.serializePunishment(punishment))
                        .setParam("STAFF", Utils.getStaffName(commandSender))
                        .setParam("TARGET", data.getName())
                        .setParam("TARGET_IP", data.getIp())
                        .setParam("TARGET_UUID", data.getUuid().toString())
                        .setParam("SILENT", String.valueOf(silent))
                        .toJSON();
                Zoom.getInstance().getRedisManager().write(json);
            } else {
                punishment.broadcast(Utils.getStaffName(commandSender), data.getName(), silent);
            }
            if (!data.isOnline()) {
                data.removeData();
            }
        });
    }
}
