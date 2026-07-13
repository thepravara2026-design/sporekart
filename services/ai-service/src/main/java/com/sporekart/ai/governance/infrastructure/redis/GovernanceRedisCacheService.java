package com.sporekart.ai.governance.infrastructure.redis;

import com.sporekart.ai.governance.config.GovernanceConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class GovernanceRedisCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final GovernanceConfig config;

    private static final String CONFIG_PREFIX = "gov:config:";
    private static final String REGISTRY_PREFIX = "gov:registry:";
    private static final String HEALTH_PREFIX = "gov:health:";
    private static final String METRICS_PREFIX = "gov:metrics:";
    private static final String VALIDATION_PREFIX = "gov:validation:";

    public void cacheConfiguration(String key, Object value) {
        redisTemplate.opsForValue().set(CONFIG_PREFIX + key, value,
            config.getCache().getConfigTtlSeconds(), TimeUnit.SECONDS);
    }

    public Object getConfiguration(String key) {
        return redisTemplate.opsForValue().get(CONFIG_PREFIX + key);
    }

    public void evictConfiguration(String key) {
        redisTemplate.delete(CONFIG_PREFIX + key);
    }

    public void cacheRegistry(String key, Object value) {
        redisTemplate.opsForValue().set(REGISTRY_PREFIX + key, value,
            config.getCache().getRegistryTtlSeconds(), TimeUnit.SECONDS);
    }

    public Object getRegistry(String key) {
        return redisTemplate.opsForValue().get(REGISTRY_PREFIX + key);
    }

    public void evictRegistry(String key) {
        redisTemplate.delete(REGISTRY_PREFIX + key);
    }

    public void cacheHealth(String key, Object value) {
        redisTemplate.opsForValue().set(HEALTH_PREFIX + key, value,
            config.getCache().getHealthTtlSeconds(), TimeUnit.SECONDS);
    }

    public Object getHealth(String key) {
        return redisTemplate.opsForValue().get(HEALTH_PREFIX + key);
    }

    public void cacheMetrics(String key, Object value) {
        redisTemplate.opsForValue().set(METRICS_PREFIX + key, value,
            config.getCache().getMetricsTtlSeconds(), TimeUnit.SECONDS);
    }

    public Object getMetrics(String key) {
        return redisTemplate.opsForValue().get(METRICS_PREFIX + key);
    }

    public void cacheValidation(String key, Object value) {
        redisTemplate.opsForValue().set(VALIDATION_PREFIX + key, value,
            config.getCache().getValidationTtlSeconds(), TimeUnit.SECONDS);
    }

    public Object getValidation(String key) {
        return redisTemplate.opsForValue().get(VALIDATION_PREFIX + key);
    }

    public void evictValidation(String key) {
        redisTemplate.delete(VALIDATION_PREFIX + key);
    }

    public void invalidateAll() {
        log.info("Invalidating all governance caches");
        redisTemplate.delete(redisTemplate.keys("gov:*"));
    }
}
