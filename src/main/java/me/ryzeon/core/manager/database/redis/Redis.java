package me.ryzeon.core.manager.database.redis;

import lombok.Getter;
import me.ryzeon.core.Zoom;
import me.ryzeon.core.manager.database.redis.handler.RedisMessagesHandler;
import me.ryzeon.core.manager.database.redis.jedis.JedisPublisher;
import me.ryzeon.core.manager.database.redis.jedis.JedisSettings;
import me.ryzeon.core.manager.database.redis.jedis.JedisSubscriber;
import org.bukkit.Bukkit;
import redis.clients.jedis.JedisPool;

@Getter
public class Redis {
    private JedisSettings settings;
    private JedisPublisher publisher;
    private JedisSubscriber subscriber;
    private JedisPool pool;
    private boolean connect;
    private String ip;
    private int port;
    private String password;

    public Redis(String Host, int port, String pass) {
        this.ip = Host;
        this.port = port;
        this.password = pass;
    }

    public void connect() {
        Zoom.getInstance().getLogger().info("Connecting to redis");
        try {
            pool = new JedisPool(ip);
            settings = new JedisSettings(ip, port, password);
            subscriber = new JedisSubscriber(JedisSubscriber.ZOOM, settings, new RedisMessagesHandler());
            publisher = new JedisPublisher(settings);
            publisher.start();
            Bukkit.getConsoleSender().sendMessage("§aSuccessfully connect to §4Redis.");
            connect = true;
        } catch (Exception exception) {
            Bukkit.getConsoleSender().sendMessage("§eAn error occurred connecting to the redis");
            connect = true;
        }
    }
}

