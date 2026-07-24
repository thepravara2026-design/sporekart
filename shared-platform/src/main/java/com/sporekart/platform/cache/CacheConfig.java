package com.sporekart.platform.cache;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableCaching
public class CacheConfig {

    private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);

    @Bean
    @Profile("!redis")
    public CacheManager localCacheManager() {
        SimpleCacheManager manager = new SimpleCacheManager();
        Collection<ConcurrentMapCache> caches = List.of(
            new ConcurrentMapCache("product-cache"),
            new ConcurrentMapCache("category-cache"),
            new ConcurrentMapCache("user-session"),
            new ConcurrentMapCache("inventory-cache"),
            new ConcurrentMapCache("knowledge-cache"),
            new ConcurrentMapCache("ai-response-cache"),
            new ConcurrentMapCache("configuration-cache"),
            new ConcurrentMapCache("token-cache"),
            new ConcurrentMapCache("rate-limit-cache"),
            new ConcurrentMapCache("idempotency-cache"),
            new ConcurrentMapCache("event-dedupe-cache")
        );
        manager.setCaches(caches);
        log.info("Local cache manager initialized with {} caches", caches.size());
        return manager;
    }

    @Bean
    @Profile("redis")
    public CacheManager redisCacheManager(org.springframework.data.redis.cache.RedisCacheConfiguration config) {
        org.springframework.data.redis.cache.RedisCacheManager.Builder builder =
            org.springframework.data.redis.cache.RedisCacheManager.builder(
                org.springframework.data.redis.connection.RedisConnectionFactory.class
            );
        return builder.cacheDefaults(config).build();
    }

    @Bean
    @Profile("redis")
    public org.springframework.data.redis.cache.RedisCacheConfiguration defaultCacheConfig() {
        return org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(5))
            .disableCachingNullValues()
            .computePrefixWith(cacheName -> "sporekart:" + cacheName + ":");
    }
}
