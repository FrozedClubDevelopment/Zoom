package club.frozed.core.command.punishment;

import club.frozed.core.Zoom;
import club.frozed.core.manager.database.redis.payload.Payload;
import club.frozed.core.manager.database.redis.payload.RedisMessage;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.PlayerOfflineData;
import club.frozed.core.manager.player.punishments.Punishment;
import club.frozed.core.manager.player.punishments.PunishmentExecutor;
import club.frozed.core.manager.player.punishments.PunishmentType;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.punishment.PunishmentUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 22/10/2020 @ 21:15
 */

public class WarnCommand extends BaseCMD {

    @Command(name = "warn",permission = "core.punishments.warn", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs cmd) {
        CommandSender commandSender = cmd.getSender();
        String[] args = cmd.getArgs();
        if (args.length == 0){
            commandSender.sendMessage(CC.translate("&e/warn <player> [reason] [-s]"));
            return;
        }
        PunishmentExecutor parameter = new PunishmentExecutor(Utils.getCommandWithIgnoreArgsOne(cmd.getArgs()), commandSender);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        PlayerData data;
        if (offlinePlayer.isOnline()) {
            data = PlayerData.getByName(offlinePlayer.getName());
        } else {
            commandSender.sendMessage(CC.translate("&eLoading player data....."));
            if (!PlayerOfflineData.hasData(offlinePlayer.getName())){
                commandSender.sendMessage(CC.translate("&cThat player doesn't have data"));
                return;
            }
            data = PlayerOfflineData.loadData(offlinePlayer.getName());
        }
        if (data == null){
            commandSender.sendMessage("&cError in have data.");
            return;
        }
        if (!parameter.validate(commandSender, data)) return;

        Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.WARN, System.currentTimeMillis(), parameter.getReason(), -1);
        parameter.invoke(commandSender, punishment);

        data.getPunishments().add(punishment);
        data.saveData();

        parameter.searchAndDestroy(data, punishment);
        if (!data.isOnline()){
            PlayerOfflineData.deleteData(data.getUuid());
        }

        if (Zoom.getInstance().getRedisManager().isActive()){
            String json = new RedisMessage(Payload.PUNISHMENTS_ADDED)
                    .setParam("PUNISHMENT", PunishmentUtil.serialize(punishment).toJson())
                    .setParam("STAFF", parameter.getStaffName(commandSender))
                    .setParam("TARGET",data.getName())
                    .setParam("TARGET_IP",data.getIp())
                    .setParam("TARGET_UUID", data.getUuid().toString())
                    .setParam("SILENT", String.valueOf(parameter.isSilent()))
                    .toJSON();
            Zoom.getInstance().getRedisManager().write(json);
        } else {
            punishment.broadcast(parameter.getStaffName(commandSender), data.getName(), parameter.isSilent());
        }
    }
}
