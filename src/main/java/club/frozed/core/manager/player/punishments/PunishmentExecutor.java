package club.frozed.core.manager.player.punishments;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.punishment.PunishmentUtil;
import club.frozed.core.utils.time.DateUtils;
import lombok.Getter;
import lombok.Setter;
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

    private boolean durationCorrect = false;

    private int reasonToStart = 2;

    public PunishmentExecutor(String[] args, CommandSender commandSender){
//        System.out.println(args.length);
        if (args.length == 1){
            this.silent = false;
            this.reason = "No Reason Provided";
            this.duration = -5;
        } else {
            if (args[1].equalsIgnoreCase("perm") || args[1].equalsIgnoreCase("permanent")) {
                this.duration = -5L;
            } else {
                this.duration = DateUtils.getDuration(args[1]);
                if (duration > 0){
                    this.durationCorrect = true;
                } else {
                    this.duration = commandSender.hasPermission("core.punishments.limit") ? -5L : TimeUnit.DAYS.toMillis(30);
                    this.reasonToStart = 1;
                }
            }
//            System.out.println(duration);
            String reason = PunishmentUtil.reasonBuilder(args, reasonToStart);
            boolean silent = reason.contains("-S") || reason.contains("-SILENT") || reason.contains("-s") || reason.contains("-silent");
            reason = PunishmentUtil.getReasonAndRemoveSilent(reason);

            this.silent = silent;
            this.reason = reason;
        }
    }

    public String getStaffName(CommandSender sender) {
        return sender instanceof Player ? ((Player) sender).getPlayer().getDisplayName() : "§4§lConsole";
    }

    public boolean validate(CommandSender sender, PlayerData playerData, PunishmentType... punishmentTypes) {
        if (reasonToStart != 2 && !durationCorrect) {
            this.duration = sender.hasPermission("core.punishments.limit") ? -5L : TimeUnit.DAYS.toMillis(30);
            return true;
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
