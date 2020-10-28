package club.frozed.core.manager.player;

import club.frozed.core.Zoom;
import club.frozed.core.manager.database.mongo.MongoManager;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 22/09/2020 @ 23:28
 */

public class PlayerOfflineData {

    @Getter public static Map<UUID, PlayerData> playerData = new HashMap<>();

    public static boolean hasData(String string){
        MongoManager mongoManager = Zoom.getInstance().getMongoManager();
        Document document = mongoManager.getPlayerData().find(Filters.eq("name", string)).first();
        return document != null;
    }
    public static boolean hasData(UUID uuid){
        MongoManager mongoManager = Zoom.getInstance().getMongoManager();
        Document document = mongoManager.getPlayerData().find(Filters.eq("uuid", uuid.toString())).first();
        if (document == null) return false;
        return document != null;
    }

    public static PlayerData createPlayerData(UUID uuid, String name) {
        if (playerData.containsKey(uuid)) return playerData.get(uuid);
        playerData.put(uuid, new PlayerData(name, uuid));
        if (!playerData.get(uuid).isDataLoaded()){
            playerData.get(uuid).loadData();
        }
        return playerData.get(uuid);
    }

    public static PlayerData getPlayerData(UUID uuid) {
        return playerData.get(uuid);
    }

    public static void deleteData(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) return;
        if (playerData.containsKey(uuid)) {
            playerData.get(uuid).saveData();
            playerData.get(uuid).removeData();
        }

        playerData.remove(uuid);
    }

    public static PlayerData loadData(String name) {
        Document document = Zoom.getInstance().getMongoManager().getPlayerData().find(Filters.eq("name", name)).first();

        if (document == null) {
            return null;
        }
        createPlayerData(UUID.fromString(document.getString("uuid")), name);
        return playerData.get(UUID.fromString(document.getString("uuid")));
    }

    public static PlayerData loadData(UUID uuid) {
        Document document = Zoom.getInstance().getMongoManager().getPlayerData().find(Filters.eq("uuid", uuid.toString())).first();

        if (document == null) {
            return null;
        }
        createPlayerData(uuid, Bukkit.getOfflinePlayer(uuid).getName());
        return playerData.get(uuid);
    }
}
