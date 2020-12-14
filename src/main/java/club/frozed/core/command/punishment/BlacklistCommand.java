package club.frozed.core.command.punishment;

import club.frozed.core.Zoom;
import club.frozed.core.manager.database.redis.payload.Payload;
import club.frozed.core.manager.database.redis.payload.RedisMessage;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.punishments.Punishment;
import club.frozed.core.manager.player.punishments.PunishmentExecutor;
import club.frozed.core.manager.player.punishments.PunishmentType;
import club.frozed.core.utils.punishment.PunishmentUtil;
import club.frozed.lib.chat.CC;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import club.frozed.lib.task.TaskUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 31/10/2020 @ 18:34
 */
public class BlacklistCommand extends BaseCommand {

    @Command(name = "blacklist", aliases = {"ipban", "banip"}, inGameOnly = false, permission = "core.punishments.blacklist")
    @Override
    public void onCommand(CommandArgs cmd) {
        CommandSender commandSender = cmd.getSender();
        String[] args = cmd.getArgs();

        TaskUtil.runAsync(() -> {
            if (args.length == 0) {
                commandSender.sendMessage(CC.translate("&c/" + cmd.getLabel() + " <player> [duration] [reason] [-s]"));
                return;
            }

            PunishmentExecutor parameter = new PunishmentExecutor(cmd.getArgs(), commandSender);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            PlayerData data;
            if (offlinePlayer.isOnline()) {
                data = PlayerData.getPlayerData(offlinePlayer.getUniqueId());
            } else {
                commandSender.sendMessage(CC.translate("&aLoading player data....."));
                data = PlayerData.loadData(offlinePlayer.getUniqueId());
                if (data == null) {
                    commandSender.sendMessage(CC.translate("&cError! &7That player doesn't have data"));
                    return;
                }
            }

            if (!parameter.validate(commandSender, data, PunishmentType.BLACKLIST)) {
                return;
            }

            Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.BLACKLIST, System.currentTimeMillis(), parameter.getReason(), parameter.getDuration());
            parameter.invoke(commandSender, punishment);

            data.getPunishments().add(punishment);
            data.saveData();

            if (Zoom.getInstance().getRedisManager().isActive()) {
                String json = new RedisMessage(Payload.PUNISHMENTS_ADDED)
                        .setParam("PUNISHMENT", PunishmentUtil.serializePunishment(punishment))
                        .setParam("STAFF", parameter.getStaffName(commandSender))
                        .setParam("TARGET", data.getName())
                        .setParam("TARGET_IP", data.getIp())
                        .setParam("TARGET_UUID", data.getUuid().toString())
                        .setParam("SILENT", String.valueOf(parameter.isSilent()))
                        .toJSON();
                Zoom.getInstance().getRedisManager().write(json);
            } else {
                parameter.searchAndDestroy(data, punishment);
                punishment.broadcast(parameter.getStaffName(commandSender), data.getName(), parameter.isSilent());
            }
            if (!data.isOnline()) {
                data.removeData();
            }
        });
    }
}
