package club.frozed.core.manager.player.punishments;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.Utils;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 26/10/2020 @ 09:41
 */

@Getter
public class PunishmentExecutor {

    private long duration;

    private String reason;

    private boolean silent;

    public PunishmentExecutor(String source, CommandSender sender){
        String[] parser = source.split(" ");
        if (parser.length != 0) {
            this.duration = Utils.parse(parser[1], TimeUnit.MILLISECONDS);
        }
        if (this.duration > 0L) {
            this.reason = parser.length <= 1 ? "" : source.replaceFirst(parser[0], "").replaceFirst(" ", "");
        } else {
            this.duration = sender.hasPermission("core.punishments.limit") ? Integer.MAX_VALUE : TimeUnit.DAYS.toMillis(30);
            this.reason = source;
        }
        silent = source.contains("-s") || source.contains("-silent") || source.contains("-SILENT") || source.contains("-S");
        this.reason = CC.strip(reason
                .replace("-S", "")
                .replace("-SILENT", "")
                .replace("-s", "")
                .replace("-silent", "")
                .replace("-sileNT", "")
                .replace(parser[0], "")
                .replaceFirst(" ", "").trim()).replaceAll("\\s+$", "");
    }

    public String getStaffName(CommandSender sender) {
        return sender instanceof Player ? ((Player) sender).getPlayer().getDisplayName() : "§4§lConsole";
    }

    public boolean validate(CommandSender sender, PlayerData playerData, PunishmentType... punishmentTypes) {
        if (duration <= 0L) {
            sender.sendMessage(CC.translate("&cError! &7You must be a provided positive number."));
            return false;
        }

        boolean cancel = true;

        for (PunishmentType punishmentType : punishmentTypes) {
            if (playerData.getActivePunishment(punishmentType) != null) {
                sender.sendMessage(CC.translate("&cError! &7That player is already " + punishmentType.getContext() + "."));
                cancel = false;
                break;
            }
        }

        if (!sender.hasPermission("core.punishments.limit") && this.duration > TimeUnit.DAYS.toMillis(30)) {
            sender.sendMessage(CC.translate("&cError! &7You can't punish players for more than 30 days."));
            cancel = false;
        }

        return cancel;
    }

    public void invoke(CommandSender sender, Punishment punishment) {
        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }
    }

    public void searchAndDestroy(PlayerData data, Punishment punishment) {
        Player player = data.getPlayer();
        if (player != null) {
            if (punishment.getType().isBannable()) {
                player.kickPlayer(punishment.toKickMessage(null));
            } else if (punishment.getType() == PunishmentType.WARN) {
                Zoom.getInstance().getPunishmentConfig().getConfig().getStringList("PUNISHMENT-MESSAGES.PLAYER.WARN").forEach(s ->
                        player.sendMessage(CC.translate(s)
                                .replace("<sender>", Utils.getDisplayName(punishment.getAddedBy()))
                                .replace("<reason>", (punishment.getReason() == null || punishment.getReason().isEmpty() || punishment.getReason().equals("") ? "No reason provided" : punishment.getReason()))
                        ));
            } else if (punishment.getType() == PunishmentType.MUTE) {
                Zoom.getInstance().getPunishmentConfig().getConfig().getStringList("PUNISHMENT-MESSAGES.PLAYER.MUTE").forEach(s ->
                        player.sendMessage(CC.translate(s)
                                .replace("<sender>", Utils.getDisplayName(punishment.getAddedBy()))
                                .replace("<duration>", punishment.getTimeLeft(false))
                                .replace("<reason>", (punishment.getReason() == null || punishment.getReason().equals("") || punishment.getReason().isEmpty() ? "No reason provided" : punishment.getReason()))
                        ));
            }
        }
    }
}
