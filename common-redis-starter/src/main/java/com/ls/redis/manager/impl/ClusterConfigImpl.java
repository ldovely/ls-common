package com.ls.redis.manager.impl;


import com.ls.redis.manager.IRedissonConfigService;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.util.StringUtils;

/**
 * 集群方式Redisson部署
 */

public class ClusterConfigImpl implements IRedissonConfigService {
    private static final Logger log = LoggerFactory.getLogger(ClusterConfigImpl.class);

    @Override
    public Config createRedissonConfig(RedisProperties redisProperties) {
        Config config = new Config();
        try {
            String password = redisProperties.getPassword();
            redisProperties.getCluster().getNodes().forEach(o -> {
                config.useClusterServers().addNodeAddress(REDIS_CONNECTION_PREFIX + o);
                if (!StringUtils.isEmpty(password)) {
                    config.useClusterServers().setPassword(password);
                }
            });

            log.info("cluster init redisson config,redisAddress:" + redisProperties);
        } catch (Exception e) {
            log.error("cluster init redisson config error", e);
        }
        return config;
    }

}
