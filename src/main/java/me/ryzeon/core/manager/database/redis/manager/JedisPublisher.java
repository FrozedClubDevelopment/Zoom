package me.ryzeon.core.manager.database.redis.manager;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import me.ryzeon.core.Zoom;
import org.apache.commons.lang.Validate;

@RequiredArgsConstructor
public class JedisPublisher {

    private final JedisSettings jedisSettings;

    public void write(String channel, JsonObject payload) {
        Validate.notNull(Zoom.getInstance().getRedis().getPool());

        Zoom.getInstance().getRedis().runCommand(redis -> {
            if (jedisSettings.hasPassword()) {
                redis.auth(jedisSettings.getPassword());
            }

            redis.publish(channel, payload.toString());

            return null;
        });
    }
}
