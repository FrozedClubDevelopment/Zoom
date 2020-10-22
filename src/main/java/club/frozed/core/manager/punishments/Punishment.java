package club.frozed.core.manager.punishments;

import club.frozed.core.Zoom;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.time.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;
import java.util.UUID;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 19/10/2020 @ 18:51
 */

@Getter @Setter
public class Punishment {

    private final UUID uuid;

    private final PunishmentType type;

    private String addedBy;

    private final long addedAt;

    private final String addedReason;

    private final long duration;

    private UUID removedBy;

    private long removedAt;

    private String removedReason;

    private boolean removed;

    @ConstructorProperties({"uuid" , "type", "addedAt" , "addedReason" , "duration"})
    public Punishment(UUID uuid, PunishmentType type, long addedAt, String addedReason, long duration) {
        this.uuid = uuid;
        this.type = type;
        this.addedAt = addedAt;
        this.addedReason = addedReason;
        this.duration = duration;
    }

    public String getAddedReason(){
        return this.addedReason == null ? "No reason provided" : this.addedReason;
    }

    public String getRemovedReason(){
        return this.removedReason == null ? "No reason provided" : this.removedReason;
    }

    public boolean isPermanent() {
        return (this.type == PunishmentType.BLACKLIST || this.duration == 2147483647L);
    }

    public boolean hasExpired() {
        return (!isPermanent() && System.currentTimeMillis() >= this.addedAt + this.duration);
    }

    public String getDurationText() {
        if (this.removed)
            return "Removed";
        if (isPermanent())
            return "Permanent";
        return TimeUtil.millisToRoundedTime(this.duration);
    }
    public String getAddedBy(){
        return this.addedBy == null ? "a staff member" : addedBy;
    }

    public String getTimeRemaining() {
        if (this.removed)
            return "Removed";
        if (isPermanent())
            return "Permanent";
        if (hasExpired())
            return "Expired";
        return TimeUtil.millisToRoundedTime(this.addedAt + this.duration - System.currentTimeMillis());
    }

    public String getContext() {
        if (this.type != PunishmentType.BAN && this.type != PunishmentType.MUTE)
            return this.removed ? this.type.getUndoContext() : this.type.getContext();
        if (isPermanent())
            return this.removed ? this.type.getUndoContext() : ("permanently " + this.type.getContext());
        return this.removed ? this.type.getUndoContext() : ("temporarily " + this.type.getContext());
    }

    public void broadcast(String sender, String target, boolean silent){
        if (silent){
            Bukkit.getServer().getOnlinePlayers().stream().filter(player -> player.hasPermission("core.punishments.silent.see")).forEach(player -> {
                Zoom.getInstance().getPunishmentConfig().getConfig().getStringList("PUNISHMENT-MESSAGES.SILENT").forEach(text ->
                        player.sendMessage(CC.translate(text)
                                .replace("<player>",target)
                                .replace("<sender>",sender)
                                .replace("<context>",getContext())
                                .replace("<reason>",getAddedReason())
                        ));
            });
        } else {
            if (Zoom.getInstance().getPunishmentConfig().getConfig().getBoolean("PUNISHMENT-MESSAGES.MESSAGE-IN-CONSOLE")){
                Zoom.getInstance().getPunishmentConfig().getConfig().getStringList("PUNISHMENT-MESSAGES.PUBLIC").forEach(text ->
                        Bukkit.broadcastMessage(CC.translate(text)
                                .replace("<player>",target)
                                .replace("<sender>",sender)
                                .replace("<context>",getContext())
                                .replace("<reason>",getAddedReason())
                        ));
            } else {
                Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                    Zoom.getInstance().getPunishmentConfig().getConfig().getStringList("PUNISHMENT-MESSAGES.PUBLIC").forEach(text ->
                            player.sendMessage(CC.translate(text)
                                    .replace("<player>",target)
                                    .replace("<sender>",sender)
                                    .replace("<context>",getContext())
                                    .replace("<reason>",getAddedReason())
                            ));
                });
            }
        }
    }

    public String getKickMessage(){
        String message = null;
        
        return message;
    }
}
