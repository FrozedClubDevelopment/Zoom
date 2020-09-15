package club.frozed.core.manager.player.rank;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by Elb1to
 * Project: Zoom
 * Date: 09/13/2020 @ 12:29
 */
@Getter
@Setter
public class Rank {

    private String name, color, prefix, suffix;
    private int priority;
    private boolean defaultRank, bold, italic;
    private List<String> permissions;

    public Rank(String name, String color, String prefix, String suffix, int priority, boolean defaultRank, List<String> permissions) {
        this.name = name;
        this.color = color;
        this.prefix = prefix;
        this.suffix = suffix;
        this.priority = priority;
        this.defaultRank = defaultRank;
        this.permissions = permissions;
    }

}
