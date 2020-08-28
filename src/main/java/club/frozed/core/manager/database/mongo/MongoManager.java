package club.frozed.zoom.manager.database.mongo;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.utils.config.ConfigCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.Collections;

@Getter
public class MongoManager {
    private MongoClient client;

    private MongoDatabase mongoDatabase;

    ConfigCursor mongoConfig = new ConfigCursor(ZoomPlugin.getInstance().getDatabaseConfig(), "MONGO");

    private final boolean authentication = mongoConfig.getBoolean("AUTH.ENABLED");
    private final String host = mongoConfig.getString("HOST");
    private final int port = mongoConfig.getInt("PORT");
    private final String authDatabase = mongoConfig.getString("AUTH-DATABASE");
    private final String database = mongoConfig.getString("DATABASE");
    private final String user = mongoConfig.getString("AUTH.USERNAME");
    private final String password = mongoConfig.getString("AUTH.PASSWORD");

    private boolean connect;

    private MongoCollection<Document> playerData;

    public void connect() {
        try {
            ZoomPlugin.getInstance().getLogger().info("Connecting to MongoDB...");
            if (authentication) {
                MongoCredential mongoCredential = MongoCredential.createCredential(this.user, this.authDatabase, this.password.toCharArray());
                this.client = new MongoClient(new ServerAddress(this.host, this.port), Collections.singletonList(mongoCredential));
                this.connect = true;
                Bukkit.getConsoleSender().sendMessage("§aSuccessfully connected to MongoDB.");
            } else {
                this.client = new MongoClient(new ServerAddress(this.host, this.port));
                this.connect = true;
                Bukkit.getConsoleSender().sendMessage("§aSuccessfully connected to MongoDB.");
            }
            this.mongoDatabase = this.client.getDatabase(this.database);
            this.playerData = this.mongoDatabase.getCollection("ZoomCore-PlayerData");
        } catch (Exception e) {
            this.connect = false;
            ZoomPlugin.getInstance().setDisableMessage("An error has occured on -> MongoDB");
            Bukkit.getConsoleSender().sendMessage("§eDisabling ZoomPlugin [Core] because an error occurred while trying to connect to MongoDB.");
            Bukkit.getPluginManager().disablePlugins();
            Bukkit.shutdown();
        }
    }

    public void reconnect() {
        try {
            if (authentication) {
                MongoCredential mongoCredential = MongoCredential.createCredential(this.user, this.authDatabase, this.password.toCharArray());
                this.client = new MongoClient(new ServerAddress(this.host, this.port), Collections.singletonList(mongoCredential));
            } else {
                this.client = new MongoClient(new ServerAddress(this.host, this.port));
            }
            this.mongoDatabase = this.client.getDatabase(this.database);
            this.playerData = this.mongoDatabase.getCollection("ZoomCore-PlayerData");
        } catch (Exception e) {
            ZoomPlugin.getInstance().setDisableMessage("An error has occured on -> MongoDB");
        }
    }

    public void disconnect() {
        if (this.client != null) {
            ZoomPlugin.getInstance().getLogger().info("[MongoDB] Disconnecting...");
            this.client.close();
            this.connect = false;
            ZoomPlugin.getInstance().getLogger().info("[MongoDB] Successfully disconnected.");
        }
    }
}
