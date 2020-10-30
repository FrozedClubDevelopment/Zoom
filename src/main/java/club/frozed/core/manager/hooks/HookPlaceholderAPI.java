package club.frozed.core.manager.hooks;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

/**
 * Created by Elb1to
 * Project: Zoom
 * Date: 09/28/2020 @ 18:50
 */
public class HookPlaceholderAPI extends PlaceholderExpansion {

    private Zoom plugin;

    public HookPlaceholderAPI(Zoom plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "zoom";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String getAuthor() {
        return "Elb1to";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        // %zoom_rank_name%
        if (identifier.equalsIgnoreCase("rank_name")) {
            if (PlayerData.getPlayerData(player.getName()).getHighestRank().getName() == null) {
                return Zoom.getInstance().getRankManager().getDefaultRank().getName();
            }
            return PlayerData.getPlayerData(player.getName()).getHighestRank().getName();
        }

        // %zoom_rank_color%
        if (identifier.equalsIgnoreCase("rank_color")) {
            return "&" + PlayerData.getPlayerData(player.getName()).getHighestRank().getColor().getChar();
        }

        // %zoom_rank_prefix%
        if (identifier.equalsIgnoreCase("rank_prefix")) {
            if (PlayerData.getPlayerData(player.getName()).getHighestRank().getPrefix() == null) {
                return Zoom.getInstance().getRankManager().getDefaultRank().getPrefix();
            }
            return PlayerData.getPlayerData(player.getName()).getHighestRank().getPrefix();
        }

        return null;
    }
}
