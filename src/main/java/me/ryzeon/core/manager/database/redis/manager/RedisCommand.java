package me.ryzeon.core.manager.database.redis.manager;

import redis.clients.jedis.Jedis;

public interface RedisCommand<T> {

    T execute(Jedis jedis);
}
