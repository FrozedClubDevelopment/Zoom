package club.frozed.core.manager.ranks;

import club.frozed.core.Zoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

import java.util.List;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 14/09/2020 @ 20:44
 * Template by Elp1to
 */

@Getter
@Setter
@AllArgsConstructor
public class Rank {

    private String name, prefix, suffix;
    private ChatColor color;
    private int priority;
    private boolean defaultRank, bold, italic;
    private List<String> permissions;

    public Rank() {

    }

    public static Rank getRankByName(String name){
        return Zoom.getInstance().getRankManager().getRanks().stream().filter(rank -> rank.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static boolean isRankExists(String rank){
        return Zoom.getInstance().getRankManager().getRanks().contains(Rank.getRankByName(rank));
    }
}