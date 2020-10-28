package club.frozed.core.command.punishment;

import club.frozed.core.Zoom;
import club.frozed.core.manager.database.redis.payload.Payload;
import club.frozed.core.manager.database.redis.payload.RedisMessage;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.PlayerOfflineData;
import club.frozed.core.manager.player.punishments.Punishment;
import club.frozed.core.manager.player.punishments.PunishmentType;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.punishment.PunishmentUtil;
import com.google.common.base.Joiner;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 26/10/2020 @ 20:40
 */

public class UnwarnCommand extends BaseCMD {

    @Command(name = "unwarn", permission = "core.punishments.unwarn", aliases = "removewarn", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs cmd) {
        CommandSender commandSender = cmd.getSender();
        String[] args = cmd.getArgs();
        if (args.length == 0){
            commandSender.sendMessage(CC.translate("&e/" + cmd.getLabel() + " <player> [reason] [-s]"));
            return;
        }
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
        Punishment punishment = data.getActivePunishment(PunishmentType.WARN);
        if (punishment == null){
            commandSender.sendMessage(CC.translate("&cError! &7That player doesn't have a warn"));
            return;
        }
        punishment.setPardonedAt(System.currentTimeMillis());
        boolean silent = false;
        if (args.length > 1){
            String reason = Joiner.on(" ").skipNulls().join(Arrays.copyOfRange(args, 1 , args.length));
            silent = reason.contains("-s") || reason.contains("-silent") || reason.contains("-SILENT") || reason.contains("-S");
            reason = reason.replace("-S", "").replace("-SILENT", "").replace("-s","").replace("-silent","");
            punishment.setPardonedReason(reason);
        }
        punishment.setPardoned(true);
        if (commandSender instanceof Player) {
            punishment.setPardonedBy(((Player) commandSender).getUniqueId());
        }
        data.saveData();
        if (!data.isOnline()){
            PlayerOfflineData.deleteData(data.getUuid());
        }

        if (Zoom.getInstance().getRedisManager().isActive()){
            String json = new RedisMessage(Payload.PUNISHMENTS_ADDED)
                    .setParam("PUNISHMENT", PunishmentUtil.serialize(punishment).toJson())
                    .setParam("STAFF", Utils.getStaffName(commandSender))
                    .setParam("TARGET",data.getName())
                    .setParam("TARGET_IP",data.getIp())
                    .setParam("TARGET_UUID", data.getUuid().toString())
                    .setParam("SILENT", String.valueOf(silent))
                    .toJSON();
            Zoom.getInstance().getRedisManager().write(json);
        } else {
            punishment.broadcast(Utils.getStaffName(commandSender), data.getName(), silent);
        }

    }
}
