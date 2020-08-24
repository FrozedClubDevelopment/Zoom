package me.ryzeon.core.manager.database;

import lombok.Getter;
import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.config.ConfigCursor;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Getter
public class RedisManager {

    JedisPool jedisPool;

    ConfigCursor databaseredis = new ConfigCursor(Zoom.getInstance().getDatabaseconfig(), "redis");

    private final String ip = databaseredis.getString("host");

    private final int port = databaseredis.getInt("port");

    private final String password = databaseredis.getString("password");

    @Getter
    private boolean connect = false;

    public void connect() {
        try {
            Zoom.getInstance().getLogger().info("Connecting to redis");
            jedisPool = new JedisPool(ip, port);
            Jedis jedis = jedisPool.getResource();
            if (password != null) {
                jedis.auth(password);
            }
            Bukkit.getConsoleSender().sendMessage("§aSuccessfully connect to §4Redis.");
            connect = true;
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§eAn error occurred connecting to the redis");
            connect = false;
        }
    }

    public void disconnect() {
        Zoom.getInstance().getLogger().info("[Redis] Disconnecting...");
        jedisPool.destroy();
        Zoom.getInstance().getLogger().info("[Redis] Disconnecting Successfully");
    }
}
