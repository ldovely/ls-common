package com.ls.redis.manager.impl;


import com.ls.redis.manager.IRedissonConfigService;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.util.StringUtils;


/**
 * @Description: 哨兵集群部署Redis连接配置
 */

public class SentineConfigImpl implements IRedissonConfigService {

    private static final Logger log = LoggerFactory.getLogger(SentineConfigImpl.class);

    @Override
    public Config createRedissonConfig(RedisProperties redisProperties) {
        Config config = new Config();
        try {
            RedisProperties.Sentinel sentinel = redisProperties.getSentinel();

            String password = redisProperties.getPassword();
            int database = redisProperties.getDatabase();

            //设置redis配置文件sentinel.conf配置的sentinel别名
            config.useSentinelServers().setMasterName(sentinel.getMaster());
            config.useSentinelServers().setDatabase(database);
            if (!StringUtils.isEmpty(password)) {
                config.useSentinelServers().setPassword(password);
            }
            sentinel.getNodes().forEach(o -> {
                config.useSentinelServers().addSentinelAddress(REDIS_CONNECTION_PREFIX + o);
            });

            log.info("sentine init redisson config,redisAddress:" + redisProperties);
        } catch (Exception e) {
            log.error("sentine init redisson config error", e);

        }
        return config;
    }

}
