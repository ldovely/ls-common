package com.ls.redis.manager.impl;


import com.ls.redis.manager.IRedissonConfigService;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.util.StringUtils;

/**
 * 单机部署Redisson配置
 */

public class StandaloneConfigImpl implements IRedissonConfigService {

    private static final Logger log = LoggerFactory.getLogger(StandaloneConfigImpl.class);

    @Override
    public Config createRedissonConfig(RedisProperties redisProperties) {
        Config config = new Config();
        try {
            String address = redisProperties.getHost() + ":" + redisProperties.getPort();
            String password = redisProperties.getPassword();
            int database = redisProperties.getDatabase();
            String redisAddr = REDIS_CONNECTION_PREFIX + address;
            config.useSingleServer().setAddress(redisAddr);
            config.useSingleServer().setDatabase(database);
            //密码可以为空
            if (!StringUtils.isEmpty(password)) {
                config.useSingleServer().setPassword(password);
            }
            log.info("standalone init Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("standalone init Config error", e);
        }
        return config;
    }
}
