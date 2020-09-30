package club.frozed.core.manager.player.grants;

import club.frozed.core.manager.ranks.Rank;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.lang.Lang;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 20/09/2020 @ 19:59
 * Template by Elp1to
 */
@Getter
@Setter
public class Grant {

    private String rankName;

    private long addedDate;

    private long duration;

    private long removedDate;

    private String addedBy;

    private String reason;

    private String removedBy;

    private boolean active;

    private boolean permanent;

    private String server = "Global";

    public Grant(String rank, long addedDate, long duration, long removedDate, String addedBy, String reason, String removedBy, boolean active, boolean permanent, String server) {
        this.rankName = rank;
        this.addedDate = addedDate;
        this.duration = duration;
        this.removedDate = removedDate;
        this.addedBy = addedBy;
        this.reason = reason;
        this.removedBy = removedBy;
        this.active = active;
        this.permanent = permanent;
        this.server = server;
    }

    public boolean hasExpired() {
        if (this.server.equalsIgnoreCase("Global") || this.server.equalsIgnoreCase(Lang.SERVER_NAME)) {
            if (!isActive())
                return true;
            if (Rank.getRankByName(this.rankName) == null)
                return true;
            if (isPermanent())
                return false;
            return (System.currentTimeMillis() >= this.addedDate + this.duration);
        }
        return true;
    }

    public String getNiceDuration() {
        if (isPermanent())
            return "Permanent";
        return Utils.formatTimeMillis(this.duration);
    }

    public String getNiceExpire() {
        if (!isActive())
            return "Expired";
        if (isPermanent())
            return "Never";
        if (hasExpired())
            return "Expired";
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.setTime(new Date(System.currentTimeMillis()));
        to.setTime(new Date(this.addedDate + getDuration()));
        return Utils.formatDateDiff(from, to);
    }

    public Rank getRank() {
        return Rank.getRankByName(this.rankName);
    }
}
