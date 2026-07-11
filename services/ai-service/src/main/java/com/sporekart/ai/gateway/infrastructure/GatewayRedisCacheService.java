package com.sporekart.ai.gateway.infrastructure;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class GatewayRedisCacheService {
    private static final long DEFAULT_TTL_MINUTES = 10;
    private static final String CONFIG_PREFIX = "gw:config:";
    private static final String FEATURE_PREFIX = "gw:features:";
    private static final String HEALTH_PREFIX = "gw:health:";

    private final StringRedisTemplate redisTemplate;

    public GatewayRedisCacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheConfiguration(String key, String value) {
        redisTemplate.opsForValue().set(CONFIG_PREFIX + key, value, DEFAULT_TTL_MINUTES, TimeUnit.MINUTES);
    }

    public String getConfiguration(String key) {
        return redisTemplate.opsForValue().get(CONFIG_PREFIX + key);
    }

    public void cacheFeatureFlag(String flagName, boolean enabled) {
        redisTemplate.opsForValue().set(FEATURE_PREFIX + flagName, String.valueOf(enabled), 5, TimeUnit.MINUTES);
    }

    public boolean getFeatureFlag(String flagName) {
        String value = redisTemplate.opsForValue().get(FEATURE_PREFIX + flagName);
        return Boolean.parseBoolean(value);
    }

    public void cacheHealthMetadata(String key, String value) {
        redisTemplate.opsForValue().set(HEALTH_PREFIX + key, value, 1, TimeUnit.MINUTES);
    }

    public String getHealthMetadata(String key) {
        return redisTemplate.opsForValue().get(HEALTH_PREFIX + key);
    }

    public void invalidateConfiguration(String key) {
        redisTemplate.delete(CONFIG_PREFIX + key);
    }

    public void invalidateAllConfiguration() {
        Set<String> keys = redisTemplate.keys(CONFIG_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public void setExecutionMetadata(String executionId, Map<String, String> metadata) {
        String key = "gw:exec:" + executionId;
        metadata.forEach((k, v) -> redisTemplate.opsForHash().put(key, k, v));
        redisTemplate.expire(key, 1, TimeUnit.HOURS);
    }

    public Map<Object, Object> getExecutionMetadata(String executionId) {
        return redisTemplate.opsForHash().entries("gw:exec:" + executionId);
    }
}
