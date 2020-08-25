package me.ryzeon.core.manager.database.redis.jedis;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import me.ryzeon.core.manager.database.redis.handler.Action;
import redis.clients.jedis.Jedis;

import java.util.LinkedList;
import java.util.Queue;

@RequiredArgsConstructor
public class JedisPublisher extends Thread {

    private final JedisSettings jedisSettings;
    private Queue<JedisQueue> queue = new LinkedList<>();

    @Override
    public void run() {
        while (true) {
            if (!this.queue.isEmpty()) {
                Jedis jedis = null;

                try {
                    jedis = this.jedisSettings.getJedisPool().getResource();

                    if (this.jedisSettings.hasPassword()) {
                        jedis.auth(this.jedisSettings.getPassword());
                    }

                    while (!this.queue.isEmpty()) {
                        JedisQueue queue = this.queue.poll();

                        JsonObject json = new JsonObject();
                        json.addProperty("action", queue.getAction().name());
                        json.add("data", queue.getData());

                        jedis.publish(queue.getChannel(), json.toString());
                    }
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
            }

            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(String channel, Action action, JsonObject data) {
        this.queue.add(new JedisQueue(channel, action, data));
    }
}

