package com.ls.redis.manager;


import org.redisson.Redisson;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RedissonManager {

    private static final Logger log = LoggerFactory.getLogger(RedissonManager.class);
    private Redisson redisson = null;

    public RedissonManager() {
    }

    public RedissonManager(Config config) {
        try {
            redisson = (Redisson) Redisson.create(config);
        } catch (Exception e) {
            log.error("Redisson init error", e);
            throw e;
        }
    }

    public Redisson getRedisson() {
        return redisson;
    }

}
