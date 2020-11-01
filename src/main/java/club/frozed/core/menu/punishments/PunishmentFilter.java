package club.frozed.core.menu.punishments;

import club.frozed.core.utils.CC;
import lombok.Getter;
import org.apache.commons.lang.WordUtils;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 26/10/2020 @ 13:43
 */

public enum PunishmentFilter {

    NONE(CC.WHITE),
    ACTIVE(CC.GREEN),
    INACTIVE(CC.RED),
    LIFETIME(CC.GOLD),
    TEMPORARILY(CC.YELLOW);

    @Getter
    private final String name;

    @Getter
    private final String color;

    private static PunishmentFilter[] values = values();

    PunishmentFilter(String color) {
        this.name = WordUtils.capitalizeFully(this.name());
        this.color = color;
    }

    public String getDisplayName() {
        return this.color + name;
    }

    public PunishmentFilter next() {
        return values[(this.ordinal() + 1) % values.length];
    }
}
