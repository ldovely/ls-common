package com.ls.redis.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义key的过期时间
 */

@ConfigurationProperties(prefix = "zhulong.cache-manager")
@Component
public class RedisCacheManagerProperties {

    private List<CacheConfig> configs;

    private String type = "standalone";

    public static class CacheConfig {
        /**
         * cache key
         */
        private String key;
        /**
         * 过期时间，sec
         */
        private long second = 60;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public long getSecond() {
            return second;
        }

        public void setSecond(long second) {
            this.second = second;
        }
    }

    public List<CacheConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<CacheConfig> configs) {
        this.configs = configs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
