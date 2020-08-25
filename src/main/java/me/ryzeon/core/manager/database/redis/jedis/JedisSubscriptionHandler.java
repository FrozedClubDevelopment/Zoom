package me.ryzeon.core.manager.database.redis.jedis;


import com.google.gson.JsonObject;

public interface JedisSubscriptionHandler {

    void handleMessage(JsonObject json);
}

