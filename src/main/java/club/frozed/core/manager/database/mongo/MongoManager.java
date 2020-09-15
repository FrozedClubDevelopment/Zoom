package club.frozed.core.manager.database.mongo;

import club.frozed.core.Zoom;
import club.frozed.core.utils.config.ConfigCursor;
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

    ConfigCursor mongoConfig = new ConfigCursor(Zoom.getInstance().getDatabaseConfig(), "MONGO");

    private final String host = mongoConfig.getString("HOST");
    private final int port = mongoConfig.getInt("PORT");
    private final String database = mongoConfig.getString("DATABASE");
    private final boolean authentication = mongoConfig.getBoolean("AUTH.ENABLED");

    private final String user = mongoConfig.getString("AUTH.USERNAME");
    private final String password = mongoConfig.getString("AUTH.PASSWORD");
    private final String authDatabase = mongoConfig.getString("AUTH.AUTH-DATABASE");

    private boolean connect;

    private MongoCollection<Document> playerData;

    private MongoCollection<Document> ranksData;

    public void connect() {
        try {
            Zoom.getInstance().getLogger().info("Connecting to MongoDB...");
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
            this.ranksData = this.mongoDatabase.getCollection("ZoomCore-RanksData");
        } catch (Exception e) {
            this.connect = false;
            Zoom.getInstance().setDisableMessage("An error has occured on -> MongoDB");
            Bukkit.getConsoleSender().sendMessage("§eDisabling Zoom [Core] because an error occurred while trying to connect to MongoDB.");
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
            Zoom.getInstance().setDisableMessage("An error has occurred on -> MongoDB");
        }
    }

    public void disconnect() {
        if (this.client != null) {
            Zoom.getInstance().getLogger().info("[MongoDB] Disconnecting...");
            this.client.close();
            this.connect = false;
            Zoom.getInstance().getLogger().info("[MongoDB] Successfully disconnected.");
        }
    }
}
