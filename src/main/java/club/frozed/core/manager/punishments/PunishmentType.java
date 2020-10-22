package club.frozed.core.manager.punishments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

import java.beans.ConstructorProperties;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 19/10/2020 @ 18:40
 */

@Getter
@AllArgsConstructor
public enum  PunishmentType {

    BLACKLIST("Blacklist", "blacklisted", "unblacklisted", true, true, new PunishmentTypeData("Blacklists", ChatColor.DARK_RED, 14)),
    BAN("Ban", "banned", "unbanned", true, true, new PunishmentTypeData("Bans", ChatColor.GOLD, 1)),
    MUTE("Mute", "muted", "unmuted", false, true, new PunishmentTypeData("Mutes", ChatColor.YELLOW, 4)),
    WARN("Warning", "warned", null, false, false, new PunishmentTypeData("Warnings", ChatColor.GREEN, 13)),
    KICK("Kick", "kicked", null, false, false, new PunishmentTypeData("Kicks", ChatColor.GRAY, 7));

    private String readable;

    private String context;

    private String undoContext;

    private boolean ban;

    private boolean removable;

    private PunishmentTypeData typeData;

    @Getter @Setter
    @AllArgsConstructor
    public static class PunishmentTypeData {
        private String readable;

        private ChatColor color;

        private int durability;
    }
}
