package club.frozed.core.manager.player.punishments;

import lombok.Getter;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 26/10/2020 @ 09:37
 */

@Getter
public enum PunishmentType {

    BLACKLIST("Blacklists", ChatColor.DARK_RED, DyeColor.RED, "blacklisted", "unblacklisted", true, true),
    BAN("Bans", ChatColor.GOLD, DyeColor.ORANGE, "banned", "unbanned", true, true),
    MUTE("Mutes", ChatColor.YELLOW, DyeColor.YELLOW, "muted", "unmuted", false, true),
    WARN("Warns", ChatColor.GREEN, DyeColor.LIME, "warned", "unwarned", false, true),
    KICK("Kicks", ChatColor.GRAY, DyeColor.GRAY, "kicked", null, false, false);

    private final String name;
    private final String pluralName;

    private final DyeColor color;
    private final ChatColor chatColor;

    private final String context;
    private final String undoContext;

    private final boolean bannable;
    private final boolean pardonable;

    PunishmentType(String puralName, ChatColor chatColor, DyeColor color, String context, String undoContext, boolean bannable, boolean pardonable) {
        this.name = WordUtils.capitalizeFully(this.name());
        this.pluralName = puralName;

        this.color = color;
        this.chatColor = chatColor;

        this.context = context;
        this.undoContext = undoContext;

        this.bannable = bannable;
        this.pardonable = pardonable;
    }
}
