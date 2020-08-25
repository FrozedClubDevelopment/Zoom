package me.ryzeon.core.manager.database.redis.manager;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import lombok.Getter;
import me.ryzeon.core.Zoom;
import me.ryzeon.core.manager.database.redis.message.Message;
import me.ryzeon.core.manager.database.redis.message.MessageListener;
import me.ryzeon.core.utils.GsonUtil;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.io.Closeable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class Redis implements Closeable {

    private Multimap<Object, Method> listeners = ArrayListMultimap.create();
    private Set<String> channels = Sets.newHashSet();

    @Getter
    private JedisPool pool;
    private String channel;
    @Getter
    private boolean connect;

    public Redis(String channel, RedisCredentials credentials) {
        this.channel = channel;
        try {
            Zoom.getInstance().getLogger().info("Connecting to redis");
            pool = new JedisPool(new JedisPoolConfig(), credentials.getHost(), credentials.getPort(), 5000);
            if (credentials.shouldAuthenticate()) {
                try (Jedis jedis = pool.getResource()) {
                    jedis.auth(credentials.getPassword());
                } catch (Exception e) {
                    Bukkit.getConsoleSender().sendMessage("§eAn error occurred connecting to auth with redis");
                }
            }
            Bukkit.getConsoleSender().sendMessage("§aSuccessfully connect to §4Redis.");
            connect = true;
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§eAn error occurred connecting to the redis");
            connect = false;
        }
    }

    /**
     * Start's a thread for receiving messages.
     */
    public void subscribe() {
        JedisPubSub jedisPubSub = new JedisPubSub() {

            @Override
            public void onMessage(String channel, String message) {
                listeners.entries().stream()
                        .filter(entry -> channel.contains(Redis.this.channel + ";" + entry.getValue().getAnnotation(Message.class).id()))
                        .forEach(entry -> {
                            try {
                                entry.getValue().invoke(entry.getKey(), GsonUtil.PLAIN_GSON.fromJson(message, entry.getValue().getParameterTypes()[0]));
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        });
            }
        };

        // Run subscription in it's own thread
        CompletableFuture.runAsync(() -> {
            runCommand(redis -> redis.subscribe(jedisPubSub, channels.toArray(new String[0])));
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return (null);
        });
    }

    /**
     * Register's the instance in which the messages will be sent.
     *
     * @param listener the listener to register.
     */
    public void registerListener(MessageListener listener) {
        if (Arrays.stream(listener.getClass().getDeclaredMethods()).noneMatch(method -> method.isAnnotationPresent(Message.class))) {
            return;
        }

        Set<Method> methods = Arrays.stream(listener.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Message.class))
                .filter(method -> method.getParameters().length == 1)
                .collect(Collectors.toSet());

        methods.forEach(method -> channels.add(this.channel + ";" + method.getAnnotation(Message.class).id()));
        listeners.putAll(listener, methods);
    }

    /**
     * Send's a packet through redis.
     *
     * @param message the packet to get sent
     */
    public void sendMessage(String channel, Object message) {
        runCommand(redis -> redis.publish(this.channel + ";" + channel, GsonUtil.PLAIN_GSON.toJson(message)));
    }

    /**
     * Send's a packet through redis.
     *
     * @param consumer the callback to be executed
     */
    private void runCommand(Consumer<Jedis> consumer) {
        Jedis jedis = Zoom.getInstance().getRedis().getPool().getResource();
        if (jedis != null) {
            consumer.accept(Zoom.getInstance().getRedis().getPool().getResource());
            pool.returnResource(Zoom.getInstance().getRedis().getPool().getResource());
        }
    }

    @Override
    public void close() {
        if (pool != null && !pool.isClosed()) {
            pool.close();
        }
    }
}
