package com.ls.redis.manager;

import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

public interface IRedissonConfigService {

    String REDIS_CONNECTION_PREFIX = "redis://";

    Config createRedissonConfig(RedisProperties redisProperties);
}
