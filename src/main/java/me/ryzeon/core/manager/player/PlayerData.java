package me.ryzeon.core.manager.player;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import me.ryzeon.core.Zoom;
import me.ryzeon.core.manager.database.mongo.MongoManager;
import me.ryzeon.core.utils.Utils;
import me.ryzeon.core.utils.time.Cooldown;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
@Setter
public class PlayerData {
    @Getter
    public static Map<UUID, PlayerData> playersdata = new HashMap<>();
    @Getter
    public static Map<String, PlayerData> playersdataNames = new HashMap<>();

    // for identiry player
    private String name;
    private UUID uuid;
    private boolean dataloaded;
    // Others things
    private String lastserver;
    private boolean staffchat;
    private boolean adminchat;
    private String country;
    private String ip;
    private Cooldown chatdelay = new Cooldown(0);
    /*
    Esto es de chat
    */
    private String tag;
    private String namecolor;
    private String chatColor;
    private boolean bold;
    private boolean italic;
    /*
    La wea del sistema de msg
     */
    private boolean togglesound;
    private boolean toggleprivatemessages;
    private List<String> ignorelist = new ArrayList<>();
    private boolean socialspy;

    public PlayerData(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
        playersdata.put(uuid, this);
        playersdataNames.put(name, this);
        this.dataloaded = false;
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
        document.put("last-server", Zoom.getInstance().getSettingsconfig().getConfig().getString("server-name"));
        document.put("staff-chat", this.staffchat);
        document.put("admin-chat", this.adminchat);
        document.put("ip", player.getAddress().getAddress().toString().replaceAll("/", ""));
        try {
            document.put("country", Utils.getCountry(player.getAddress().getAddress().toString().replaceAll("/", "")));
        } catch (Exception e) {
            Bukkit.getLogger().info("Error in get player country");
        }
        document.put("tag", this.tag);
        document.put("name-color", this.namecolor);
        document.put("chat-color", this.chatColor);
        document.put("name-color-bold", this.bold);
        document.put("name-color-italic", this.italic);
        /*
        pa los msg
         */
        document.put("toggle-sounds", this.togglesound);
        document.put("toggle-privatemsg", this.toggleprivatemessages);
        document.put("ignore-list", this.ignorelist);
        document.put("social-spy", this.socialspy);
        this.dataloaded = false;
        playersdata.remove(uuid);
        playersdataNames.remove(name);
        MongoManager mongoManager = Zoom.getInstance().getMongoManager();
        mongoManager.getPlayerdata().replaceOne(Filters.eq("name", this.name), document, (new UpdateOptions()).upsert(true));
    }

    public void loadData() {
        MongoManager mongoManager = Zoom.getInstance().getMongoManager();
        Document document = mongoManager.getPlayerdata().find(Filters.eq("name", this.name)).first();
        if (document != null) {
            this.lastserver = document.getString("last-server");
            this.staffchat = document.getBoolean("staff-chat");
            this.adminchat = document.getBoolean("admin-chat");
            this.country = document.getString("country");
            this.ip = document.getString("ip");
            this.tag = document.getString("tag");
            this.namecolor = document.getString("name-color");
            this.chatColor = document.getString("chat-color");
            this.bold = document.getBoolean("name-color-bold");
            this.italic = document.getBoolean("name-color-italic");
            /*
            pal chat seetings
             */
            this.togglesound = document.getBoolean("toggle-sounds");
            this.toggleprivatemessages = document.getBoolean("toggle-privatemsg");
            this.ignorelist.addAll((List<String>) document.get("ignore-list"));
            this.socialspy = document.getBoolean("social-spy");
        }
        this.dataloaded = true;
        Zoom.getInstance().getLogger().info(PlayerData.this.getName() + "'s data was successfully loaded.");
    }

    public void destroy() {
        this.saveData();
    }

    public static PlayerData getByUuid(UUID uuid) {
        return playersdata.get(uuid);
    }

    public static PlayerData getByName(String name) {
        return playersdataNames.get(name);
    }
}
