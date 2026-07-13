package com.sporekart.ai.policy.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyRedisCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String REGISTRY_PREFIX = "policy:registry:";
    private static final String COMPILED_PREFIX = "policy:compiled:";
    private static final String METADATA_PREFIX = "policy:metadata:";
    private static final String EVALUATION_PREFIX = "policy:evaluation:";
    private static final String HEALTH_PREFIX = "policy:health:";

    private static final long REGISTRY_TTL = 300;
    private static final long COMPILED_TTL = 600;
    private static final long METADATA_TTL = 300;
    private static final long EVALUATION_TTL = 180;
    private static final long HEALTH_TTL = 60;

    public void cacheRegistry(String key, Object value) {
        redisTemplate.opsForValue().set(REGISTRY_PREFIX + key, value, REGISTRY_TTL, TimeUnit.SECONDS);
    }

    public Object getRegistry(String key) {
        return redisTemplate.opsForValue().get(REGISTRY_PREFIX + key);
    }

    public void evictRegistry(String key) {
        redisTemplate.delete(REGISTRY_PREFIX + key);
    }

    public void cacheCompiled(String key, Object value) {
        redisTemplate.opsForValue().set(COMPILED_PREFIX + key, value, COMPILED_TTL, TimeUnit.SECONDS);
    }

    public Object getCompiled(String key) {
        return redisTemplate.opsForValue().get(COMPILED_PREFIX + key);
    }

    public void evictCompiled(String key) {
        redisTemplate.delete(COMPILED_PREFIX + key);
    }

    public void cacheMetadata(String key, Object value) {
        redisTemplate.opsForValue().set(METADATA_PREFIX + key, value, METADATA_TTL, TimeUnit.SECONDS);
    }

    public Object getMetadata(String key) {
        return redisTemplate.opsForValue().get(METADATA_PREFIX + key);
    }

    public void cacheEvaluation(String key, Object value) {
        redisTemplate.opsForValue().set(EVALUATION_PREFIX + key, value, EVALUATION_TTL, TimeUnit.SECONDS);
    }

    public Object getEvaluation(String key) {
        return redisTemplate.opsForValue().get(EVALUATION_PREFIX + key);
    }

    public void cacheHealth(String key, Object value) {
        redisTemplate.opsForValue().set(HEALTH_PREFIX + key, value, HEALTH_TTL, TimeUnit.SECONDS);
    }

    public Object getHealth(String key) {
        return redisTemplate.opsForValue().get(HEALTH_PREFIX + key);
    }

    public void invalidateAll() {
        log.info("Invalidating all policy caches");
        redisTemplate.delete(redisTemplate.keys("policy:*"));
    }
}
