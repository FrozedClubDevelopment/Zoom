package me.ryzeon.core.manager.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.config.ConfigCursor;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.Collections;

@Getter
public class MongoManager {
    private MongoClient client;

    private MongoDatabase mongoDatabase;

    ConfigCursor mongoconfig = new ConfigCursor(Zoom.getInstance().getDatabaseconfig(), "mongodb");

    private final boolean authentication = mongoconfig.getBoolean("authentication.enabled");

    private final String host = mongoconfig.getString("host");

    private final int port = mongoconfig.getInt("port");

    private final String authdatabase = mongoconfig.getString("database");

    private final String database = mongoconfig.getString("database");

    private final String user = mongoconfig.getString("authentication.user");

    private final String password = mongoconfig.getString("authentication.password");

    private boolean connect;

    private MongoCollection<Document> playerdata;

    public void connect() {
        try {
            Zoom.getInstance().getLogger().info("Connecting to database");
            if (authentication) {
                MongoCredential mongoCredential = MongoCredential.createCredential(this.user, this.authdatabase, this.password.toCharArray());
                this.client = new MongoClient(new ServerAddress(this.host, this.port), Collections.singletonList(mongoCredential));
                this.connect = true;
                Bukkit.getConsoleSender().sendMessage("§aSuccessfully connect to MongoDB.");
            } else {
                this.client = new MongoClient(new ServerAddress(this.host, this.port));
                this.connect = true;
                Bukkit.getConsoleSender().sendMessage("§aSuccessfully connect to MongoDB.");
            }
            this.mongoDatabase = this.client.getDatabase(this.database);
            this.playerdata = this.mongoDatabase.getCollection("ZoomCore-PlayerData");
        } catch (Exception e) {
            this.connect = false;
            Zoom.getInstance().setDisablemsg("Error in mongodb");
            Bukkit.getConsoleSender().sendMessage("§eDisabling Zoom core because an error occurred connecting to the database");
            Bukkit.getPluginManager().disablePlugins();
            Bukkit.shutdown();
        }
    }
    public void disconnect(){
        if (this.client != null){
            Zoom.getInstance().getLogger().info("[DB] Disconnecting...");
            this.client.close();
            this.connect = false;
            Zoom.getInstance().getLogger().info("[DB] Disconnecting Successfully");
        }
    }
}
