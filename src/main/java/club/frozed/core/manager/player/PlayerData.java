package club.frozed.core.manager.player;

import club.frozed.core.Zoom;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.lang.Lang;
import club.frozed.core.utils.time.Cooldown;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import club.frozed.core.manager.database.mongo.MongoManager;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter @Setter
public class PlayerData {

    @Getter public static Map<UUID, PlayerData> playersData = new HashMap<>();
    @Getter public static Map<String, PlayerData> playersDataNames = new HashMap<>();

    // Player identification
    private String name;
    private UUID uuid;
    private boolean dataLoaded;

    // Others things
    private String lastServer;
    private boolean staffChat;
    private boolean adminChat;
    private String country;
    private String ip;
    private Cooldown chatDelay = new Cooldown(0);

    // Chat Stuff
    private String tag;
    private String nameColor;
    private String chatColor;
    private boolean bold;
    private boolean italic;

    // Messages System
    private boolean toggleSounds;
    private boolean togglePrivateMessages;
    private List<String> ignoredPlayersList = new ArrayList<>();
    private boolean socialSpy;

    public PlayerData(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
        playersData.put(uuid, this);
        playersDataNames.put(name, this);
        this.dataLoaded = false;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public void saveData() {
        Document document = new Document();
        Player player = Bukkit.getPlayer(uuid);
        document.put("name", this.name);
        document.put("name_lowercase", player.getName().toLowerCase());
        document.put("uuid", getUuid().toString());
        document.put("last-server", Lang.SERVER_NAME);
        document.put("staff-chat", this.staffChat);
        document.put("admin-chat", this.adminChat);
        document.put("ip", player.getAddress().getAddress().toString().replaceAll("/", ""));
        try {
            document.put("country", Utils.getCountry(player.getAddress().getAddress().toString().replaceAll("/", "")));
        } catch (Exception e) {
            Bukkit.getLogger().info("Error in get player country");
        }
        document.put("tag", this.tag);
        document.put("name-color", this.nameColor);
        document.put("chat-color", this.chatColor);
        document.put("name-color-bold", this.bold);
        document.put("name-color-italic", this.italic);
        /*
        pa los msg
         */
        document.put("social-spy", this.socialSpy);
        document.put("toggle-sounds", this.toggleSounds);
        document.put("toggle-privatemsg", this.togglePrivateMessages);
        document.put("ignore-list", this.ignoredPlayersList);
        this.dataLoaded = false;
        playersData.remove(uuid);
        playersDataNames.remove(name);
        MongoManager mongoManager = Zoom.getInstance().getMongoManager();
        mongoManager.getPlayerData().replaceOne(Filters.eq("name", this.name), document, (new UpdateOptions()).upsert(true));
    }

    public void loadData() {
        MongoManager mongoManager = Zoom.getInstance().getMongoManager();
        Document document = mongoManager.getPlayerData().find(Filters.eq("name", this.name)).first();
        if (document != null) {
            this.lastServer = document.getString("last-server");
            this.staffChat = document.getBoolean("staff-chat");
            this.adminChat = document.getBoolean("admin-chat");
            this.country = document.getString("country");
            this.ip = document.getString("ip");
            this.tag = document.getString("tag");
            this.nameColor = document.getString("name-color");
            this.chatColor = document.getString("chat-color");
            this.bold = document.getBoolean("name-color-bold");
            this.italic = document.getBoolean("name-color-italic");

            // Private Player Chat Settings
            this.socialSpy = document.getBoolean("social-spy");
            this.toggleSounds = document.getBoolean("toggle-sounds");
            this.togglePrivateMessages = document.getBoolean("toggle-privatemsg");
            this.ignoredPlayersList.addAll((List<String>) document.get("ignore-list"));
        }
        this.dataLoaded = true;
        Zoom.getInstance().getLogger().info(PlayerData.this.getName() + "'s data was successfully loaded.");
    }

    public void destroy() {
        this.saveData();
    }

    public static PlayerData getByUuid(UUID uuid) {
        return playersData.get(uuid);
    }

    public static PlayerData getByName(String name) {
        return playersDataNames.get(name);
    }
}
