package club.frozed.core.manager.database.redis;

import club.frozed.core.Zoom;
import club.frozed.core.manager.database.redis.payload.RedisListener;
import club.frozed.lib.config.ConfigCursor;
import lombok.Getter;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 8/09/2020 @ 18:38
 * Template by Elp1to
 */

@Getter
public class RedisManager {

    JedisPool jedisPool;

    RedisListener redisListener;

    ConfigCursor databaseRedis = new ConfigCursor(Zoom.getInstance().getDatabaseConfig(), "REDIS");

    private final String ip = databaseRedis.getString("HOST");

    private final int port = databaseRedis.getInt("PORT");

    private final String password = databaseRedis.getString("AUTHENTICATION.PASSWORD");

    private final boolean auth = databaseRedis.getBoolean("AUTHENTICATION.ENABLED");

    @Getter
    private boolean active = false;

    public void connect() {
        try {
            Zoom.getInstance().getLogger().info("Connecting to redis");
            this.jedisPool = new JedisPool(this.ip, this.port);
            Jedis jedis = this.jedisPool.getResource();
            if (auth){
                if (password != null || !password.equals(""))
                jedis.auth(this.password);
            }
            this.redisListener = new RedisListener();
            (new Thread(() -> jedis.subscribe(this.redisListener, "Zoom"))).start();
            jedis.connect();
            active = true;
            Bukkit.getConsoleSender().sendMessage("§aSuccessfully connect to §4Redis.");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§6[Zoom] An error occurred while trying to connect to Redis.");
            active = false;
        }
    }

    public void disconnect() {
        Zoom.getInstance().getLogger().info("[Redis] Disconnecting...");
        this.redisListener.unsubscribe();
        jedisPool.getResource().close();
        jedisPool.destroy();
        Zoom.getInstance().getLogger().info("[Redis] Disconnecting Successfully");
    }

    public void write(String json){
        Jedis jedis = this.jedisPool.getResource();
        try {
            if (auth){
                if (password != null || !password.equals(""))
                    jedis.auth(this.password);
            }
            jedis.publish("Zoom",json);
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }
}