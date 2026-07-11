package com.sporekart.ai.provider.infrastructure;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class ProviderRedisCacheService {
    private static final long DEFAULT_TTL_MINUTES = 10;
    private static final String REGISTRY_PREFIX = "prov:registry:";
    private static final String CAPABILITIES_PREFIX = "prov:capabilities:";
    private static final String MODELS_PREFIX = "prov:models:";
    private static final String HEALTH_PREFIX = "prov:health:";
    private static final String CONFIG_PREFIX = "prov:config:";

    private final StringRedisTemplate redisTemplate;

    public ProviderRedisCacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheRegistry(String providerName, String value) {
        redisTemplate.opsForValue().set(REGISTRY_PREFIX + providerName, value, DEFAULT_TTL_MINUTES, TimeUnit.MINUTES);
    }

    public String getRegistry(String providerName) {
        return redisTemplate.opsForValue().get(REGISTRY_PREFIX + providerName);
    }

    public void cacheCapabilities(String providerName, String value) {
        redisTemplate.opsForValue().set(CAPABILITIES_PREFIX + providerName, value, DEFAULT_TTL_MINUTES, TimeUnit.MINUTES);
    }

    public String getCapabilities(String providerName) {
        return redisTemplate.opsForValue().get(CAPABILITIES_PREFIX + providerName);
    }

    public void cacheModels(String providerName, String modelsJson) {
        redisTemplate.opsForValue().set(MODELS_PREFIX + providerName, modelsJson, 5, TimeUnit.MINUTES);
    }

    public String getModels(String providerName) {
        return redisTemplate.opsForValue().get(MODELS_PREFIX + providerName);
    }

    public void cacheHealth(String providerName, String healthJson) {
        redisTemplate.opsForValue().set(HEALTH_PREFIX + providerName, healthJson, 1, TimeUnit.MINUTES);
    }

    public String getHealth(String providerName) {
        return redisTemplate.opsForValue().get(HEALTH_PREFIX + providerName);
    }

    public void cacheConfiguration(String providerName, String configJson) {
        redisTemplate.opsForValue().set(CONFIG_PREFIX + providerName, configJson, DEFAULT_TTL_MINUTES, TimeUnit.MINUTES);
    }

    public String getConfiguration(String providerName) {
        return redisTemplate.opsForValue().get(CONFIG_PREFIX + providerName);
    }

    public void invalidateProvider(String providerName) {
        redisTemplate.delete(REGISTRY_PREFIX + providerName);
        redisTemplate.delete(CAPABILITIES_PREFIX + providerName);
        redisTemplate.delete(MODELS_PREFIX + providerName);
        redisTemplate.delete(HEALTH_PREFIX + providerName);
        redisTemplate.delete(CONFIG_PREFIX + providerName);
    }

    public void invalidateAll() {
        Set<String> keys = redisTemplate.keys("prov:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
