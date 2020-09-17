package club.frozed.core.manager.ranks;

import club.frozed.core.Zoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 14/09/2020 @ 20:44
 * Template by Elp1to
 */

@Getter
@Setter
public class Rank {

    @Getter public static List<Rank> ranks = new ArrayList<>();

    private String name, prefix, suffix;
    private ChatColor color;
    private int priority;
    private boolean defaultRank, bold, italic;
    private List<String> permissions;

    public Rank(String name, String prefix, String suffix, ChatColor rankColor, int priority, boolean defaultRank, boolean bold, boolean italic, List<String> permissions) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.color = rankColor;
        this.priority = priority;
        this.defaultRank = defaultRank;
        this.bold = bold;
        this.italic = italic;
        this.permissions = permissions;
        ranks.add(this);
    }

    public static Rank getRankByName(String name){
        return ranks.stream().filter(rank -> rank.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static boolean isRankExist(String rank){
        return ranks.contains(Rank.getRankByName(rank));
    }
}