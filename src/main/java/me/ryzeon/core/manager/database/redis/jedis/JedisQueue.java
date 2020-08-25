package me.ryzeon.core.manager.database.redis.jedis;

import com.google.gson.JsonObject;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.ryzeon.core.manager.database.redis.handler.Action;

@Data
@RequiredArgsConstructor
public class JedisQueue {

    @NonNull
    private String channel;
    @NonNull
    private Action action;
    @NonNull
    private JsonObject data;

}
