package com.sporekart.ai.application.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
public class AiCacheService {
    private static final long DEFAULT_TTL_MINUTES = 10;
    private final StringRedisTemplate redisTemplate;

    public AiCacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void put(String key, String value) {
        redisTemplate.opsForValue().set(key, value, DEFAULT_TTL_MINUTES, TimeUnit.MINUTES);
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public String getOrPut(String key, Supplier<String> valueSupplier) {
        String cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;
        }
        String value = valueSupplier.get();
        if (value != null) {
            redisTemplate.opsForValue().set(key, value, DEFAULT_TTL_MINUTES, TimeUnit.MINUTES);
        }
        return value;
    }

    public void evict(String key) {
        redisTemplate.delete(key);
    }
}
