package club.frozed.core.manager.ranks;

import club.frozed.core.Zoom;
import club.frozed.core.manager.database.mongo.MongoManager;
import club.frozed.core.manager.player.PlayerData;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
    private List<String> permissions, inheritance;

    public Rank(String name, String prefix, String suffix, ChatColor rankColor, int priority, boolean defaultRank, boolean bold, boolean italic, List<String> permissions, List<String> inheritance) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.color = rankColor;
        this.priority = priority;
        this.defaultRank = defaultRank;
        this.bold = bold;
        this.italic = italic;
        this.permissions = permissions;
        this.inheritance = inheritance;
        ranks.add(this);
    }

    public void update() {
        try {
            Document document = new Document();
            document.put("NAME", this.name);
            document.put("PREFIX", this.prefix);
            document.put("SUFFIX", this.suffix);
            document.put("COLOR", this.color.name());
            document.put("PRIORITY", this.priority);
            document.put("DEFAULT", this.defaultRank);
            document.put("BOLD", this.bold);
            document.put("ITALIC", this.italic);
            document.put("INHERITANCE", this.inheritance);
            document.put("PERMISSIONS", this.permissions);
            ranks.remove(this);
            MongoManager mongoManager = Zoom.getInstance().getMongoManager();
            mongoManager.getRanksData().replaceOne(Filters.eq("NAME", this.name), document, (new UpdateOptions()).upsert(true));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            Document document = Zoom.getInstance().getMongoManager().getRanksData().find(Filters.eq("NAME", this.name)).first();
            String rankName = document.getString("NAME");
            String rankPrefix = document.getString("PREFIX");
            String rankSuffix = document.getString("SUFFIX");
            ChatColor rankColor;
            if (document.getString("COLOR") == null){
                rankColor = ChatColor.WHITE;
            } else {
                rankColor = ChatColor.valueOf(document.getString("COLOR"));
            }
            boolean rankDefault = document.getBoolean("DEFAULT");
            boolean rankBold = document.getBoolean("BOLD");
            boolean rankItalic = document.getBoolean("ITALIC");
            int rankPriority = document.getInteger("PRIORITY");

            List<String> rankInheritance = (List<String>) document.get("INHERITANCE");
            List<String> rankPermission = (List<String>) document.get("PERMISSIONS");
            new Rank(rankName, rankPrefix, rankSuffix, rankColor, rankPriority, rankDefault, rankBold, rankItalic, rankPermission, rankInheritance);
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
        }
    }

    public String formatName(Player player) {
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        String nameColor = this.getColor().toString();
        if (playerData != null && playerData.getNameColor() != null) {
            nameColor = ChatColor.valueOf(playerData.getNameColor()).toString();
        }
        if (this.isItalic() && this.isBold()) {
            return nameColor + ChatColor.BOLD.toString() + ChatColor.ITALIC.toString() + player.getName();
        }
        if (this.isBold() && this.isItalic()){
            return nameColor + ChatColor.BOLD.toString() + ChatColor.ITALIC.toString() + player.getName();
        }
        if (this.isBold()) {
            return nameColor + ChatColor.BOLD.toString() + player.getName();
        }
        if (this.isItalic()) {
            return nameColor + ChatColor.ITALIC.toString() + player.getName();
        }
        return nameColor + player.getName();
    }

    public static Rank getRankByName(String name) {
        return ranks.stream().filter(rank -> rank.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static boolean isRankExist(String rank) {
        return ranks.contains(Rank.getRankByName(rank));
    }

    public boolean hasPermission(String value) {
        return (this.permissions.stream().filter(permission -> permission.equalsIgnoreCase(value)).findFirst().orElse(null) != null);
    }

    public boolean hasInheritance(String value) {
        return (this.inheritance.stream().filter(inheritance -> inheritance.equalsIgnoreCase(value)).findFirst().orElse(null) != null);
    }
}