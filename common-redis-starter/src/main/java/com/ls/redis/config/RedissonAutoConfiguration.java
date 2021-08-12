package com.ls.redis.config;

import com.ls.redis.manager.IRedissonConfigService;
import com.ls.redis.manager.RedissonManager;
import com.ls.redis.manager.impl.ClusterConfigImpl;
import com.ls.redis.manager.impl.SentineConfigImpl;
import com.ls.redis.manager.impl.StandaloneConfigImpl;
import com.ls.redis.util.RedissonLock;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@ConditionalOnClass(Redisson.class)
@EnableConfigurationProperties({RedisProperties.class, RedisCacheManagerProperties.class})
public class RedissonAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RedissonAutoConfiguration.class);

    @Resource
    private RedisCacheManagerProperties redisCacheManagerProperties;

    @Resource
    private RedisProperties redisProperties;


    @Bean
    @ConditionalOnMissingBean
    public Config config() {
        if (log.isDebugEnabled() && null == redisCacheManagerProperties.getType()) {
            log.debug("redisson lock type is not set,use default standalone");
        }

        IRedissonConfigService configService;
        switch (redisCacheManagerProperties.getType()) {
            case "sentine":
                configService = new SentineConfigImpl();
                break;
            case "cluster":
                configService = new ClusterConfigImpl();
                break;
            default:
                configService = new StandaloneConfigImpl();
                break;
        }

        return configService.createRedissonConfig(redisProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public RedissonManager redissonManager(Config config) {
        return new RedissonManager(config);
    }

    @Bean
    @ConditionalOnMissingBean
    public RedissonLock getRedissonLock(RedissonManager redissonManager) {
        return new RedissonLock(redissonManager);
    }
}
