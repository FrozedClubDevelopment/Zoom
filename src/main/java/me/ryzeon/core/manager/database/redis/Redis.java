package me.ryzeon.core.manager.database.redis;

import com.google.gson.JsonObject;
import lombok.Getter;
import me.ryzeon.core.Zoom;
import me.ryzeon.core.manager.database.redis.handler.Payload;
import me.ryzeon.core.manager.database.redis.handler.RedisSubHandler;
import me.ryzeon.core.manager.database.redis.manager.JedisPublisher;
import me.ryzeon.core.manager.database.redis.manager.JedisSettings;
import me.ryzeon.core.manager.database.redis.manager.JedisSubscriber;
import me.ryzeon.core.manager.database.redis.manager.RedisCommand;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Getter
public class Redis {

    private JedisSettings settings;
    private JedisPool pool;
    private JedisPublisher publisher;
    private JedisSubscriber subscriber;

    public Redis(JedisSettings settings) {
        this.settings = settings;
        this.pool = new JedisPool(this.settings.getAddress(), this.settings.getPort());
        try (redis.clients.jedis.Jedis jedis = this.pool.getResource()) {
            if (this.settings.hasPassword()) {
                jedis.auth(this.settings.getPassword());
            }
            this.publisher = new JedisPublisher(this.settings);
            this.subscriber = new JedisSubscriber("Zoom", this.settings, new RedisSubHandler());
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§cError in connect to redis");
        }
    }

    public static Redis getInstance() {
        return Zoom.getInstance().getRedis();
    }

    public boolean isActive() {
        return this.pool != null && !this.pool.isClosed();
    }

    public void write(Payload payload, JsonObject data) {
        JsonObject object = new JsonObject();

        object.addProperty("payload", payload.name());
        object.add("data", data == null ? new JsonObject() : data);

        this.publisher.write("Zoom", object);
    }

    public <T> T runCommand(RedisCommand<T> redisCommand) {
        Jedis jedis = Zoom.getInstance().getRedis().getPool().getResource();
        T result = null;
        try {
            result = redisCommand.execute(jedis);
        } catch (Exception e) {
//            e.printStackTrace();
            if (jedis != null) {
                this.pool.returnBrokenResource(jedis);
            }
        }
//        finally {
//            if (jedis != null) {
//                try {
//                    this.pool.returnResource(jedis);
//                } catch (Exception exception) {
////                    exception.printStackTrace();
//                }
//            }
//        }
        return result;
    }
}
