package club.frozed.core.manager.ranks;

import club.frozed.core.Zoom;
import club.frozed.core.utils.config.FileConfig;
import club.frozed.core.utils.lang.Lang;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 14/09/2020 @ 20:44
 * Template by Elp1to
 */

public class RankManager {

    public void loadRanks() {
        if (Zoom.getInstance().getMongoManager().getRanksData().find().into(new ArrayList<>()).isEmpty()){
            loadRanksFromConfig();
        }
        else {
            loadRanksFromMongo();
        }
    }

    public void loadRanksFromConfig() {
            Rank.ranks.clear();
            try {
                for (String rank : Zoom.getInstance().getRanksConfig().getConfig().getKeys(false)) {
                    String rankName = Zoom.getInstance().getRanksConfig().getConfig().getString(rank + ".NAME");
                    String rankPrefix = Zoom.getInstance().getRanksConfig().getConfig().getString(rank + ".PREFIX");
                    String rankSuffix = Zoom.getInstance().getRanksConfig().getConfig().getString(rank + ".SUFFIX");
                    ChatColor rankColor = ChatColor.valueOf(Zoom.getInstance().getRanksConfig().getConfig().getString(rank + ".COLOR"));
                    if (rankColor == null){
                        rankColor = ChatColor.WHITE;
                    }
                    boolean rankDefault = Zoom.getInstance().getRanksConfig().getConfig().getBoolean(rank + ".DEFAULT");
                    boolean rankBold = Zoom.getInstance().getRanksConfig().getConfig().getBoolean(rank + ".BOLD");
                    boolean rankItalic = Zoom.getInstance().getRanksConfig().getConfig().getBoolean(rank + ".ITALIC");
                    int rankPriority = Zoom.getInstance().getRanksConfig().getConfig().getInt(rank + ".PRIORITY");
                    List<String> rankPermission = Zoom.getInstance().getRanksConfig().getConfig().getStringList(rank + ".PERMISSIONS");

                    new Rank(rankName, rankPrefix, rankSuffix, rankColor, rankPriority, rankDefault, rankBold, rankItalic, rankPermission);
                }
                Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§eSuccessfully loaded §f" + Rank.ranks.size() + " §eranks from Ranks.yml");
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§cAn error occurred while loading the ranks. Please check your config!");
                Bukkit.shutdown();
            }
    }

    public void saveRanks() {
        Document document = new Document();
        try {
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§aSaving ranks in MongoDB.");
            for (Rank rank : Rank.ranks) {
                document.put("NAME", rank.getName());
                document.put("PREFIX", rank.getPrefix());
                document.put("SUFFIX", rank.getSuffix());
                document.put("COLOR", rank.getColor().name());
                document.put("PRIORITY", rank.getPriority());
                document.put("DEFAULT", rank.isDefaultRank());
                document.put("BOLD", rank.isBold());
                document.put("ITALIC", rank.isItalic());
                document.put("PERMISSIONS", rank.getPermissions());

                Zoom.getInstance().getMongoManager().getRanksData().replaceOne(Filters.eq("NAME", rank.getName()), document, (new UpdateOptions()).upsert(true));
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§cAn error occurred while saving the ranks. Please check your config!");
        }
    }

    public void loadRanksFromMongo(){
        Rank.ranks.clear();
        try {
            List<Document> documents = Zoom.getInstance().getMongoManager().getRanksData().find().into(new ArrayList<>());
            for (Document document : documents) {
                String rankName = document.getString("NAME");
                String rankPrefix = document.getString("PREFIX");
                String rankSuffix = document.getString("SUFFIX");
                ChatColor rankColor = ChatColor.valueOf(document.getString("COLOR"));
                if (rankColor == null) {
                    rankColor = ChatColor.WHITE;
                }
                boolean rankDefault = document.getBoolean("DEFAULT");
                boolean rankBold = document.getBoolean("BOLD");
                boolean rankItalic = document.getBoolean("ITALIC");
                int rankPriority = document.getInteger("PRIORITY");

                List<String> rankPermission = (List<String>) document.get("PERMISSIONS");
                new Rank(rankName, rankPrefix, rankSuffix, rankColor, rankPriority, rankDefault, rankBold, rankItalic, rankPermission);
            }
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§eSuccessfully loaded §f" + Rank.ranks.size() + " §eranks from MongoDB");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§cAn error occurred while loading the ranks. Please check your MongoDB!");
            Bukkit.shutdown();
        }
    }

    public void saveFromMongo(){
        List<Document> documents = Zoom.getInstance().getMongoManager().getRanksData().find().into(new ArrayList<>());
        for (Document document : documents){
            String rankName = document.getString("NAME");
            String rankPrefix = document.getString("PREFIX");
            String rankSuffix = document.getString("SUFFIX");
            ChatColor rankColor = ChatColor.valueOf(document.getString("COLOR"));
            if (rankColor == null){
                rankColor = ChatColor.WHITE;
            }
            boolean rankDefault = document.getBoolean("DEFAULT");
            boolean rankBold = document.getBoolean("BOLD");
            boolean rankItalic = document.getBoolean("ITALIC");
            int rankPriority = document.getInteger("PRIORITY");;
            List<String> rankPermission = (List<String>) document.get("PERMISSIONS");

            FileConfig config = Zoom.getInstance().getRanksConfig();

            config.getConfig().set(rankName + ".NAME", rankName);
            config.getConfig().set(rankName + ".PREFIX", rankPrefix);
            config.getConfig().set(rankName + ".SUFFIX", rankSuffix);
            config.getConfig().set(rankName + ".COLOR", rankColor.name());
            config.getConfig().set(rankName + ".DEFAULT", rankDefault);
            config.getConfig().set(rankName + ".BOLD", rankBold);
            config.getConfig().set(rankName + ".ITALIC", rankItalic);
            config.getConfig().set(rankName + ".PRIORITY", rankPriority);
            config.getConfig().set(rankName + ".PERMISSIONS", rankPermission);

            config.save();
        }
    }
}
