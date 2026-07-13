package com.sporekart.ai.automation.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Profile("!test")
@Slf4j
@Service
public class AutomationRedisCacheService {

    private static final String WORKFLOW_PREFIX = "automation:workflow:";
    private static final String SCHEDULER_PREFIX = "automation:scheduler:";
    private static final String LIFECYCLE_PREFIX = "automation:lifecycle:";
    private static final String CONFIG_PREFIX = "automation:config:";
    private static final String STATISTICS_PREFIX = "automation:statistics:";

    private static final long WORKFLOW_TTL = 300;
    private static final long SCHEDULER_TTL = 300;
    private static final long LIFECYCLE_TTL = 600;
    private static final long CONFIG_TTL = 300;
    private static final long STATISTICS_TTL = 120;

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;
    private final Counter cacheWriteCounter;

    public AutomationRedisCacheService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper,
                                       MeterRegistry meterRegistry) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cacheHitCounter = Counter.builder("automation.cache.hits")
                .description("Automation cache hit count").register(meterRegistry);
        this.cacheMissCounter = Counter.builder("automation.cache.misses")
                .description("Automation cache miss count").register(meterRegistry);
        this.cacheWriteCounter = Counter.builder("automation.cache.writes")
                .description("Automation cache write count").register(meterRegistry);
    }

    public void cacheWorkflow(String key, String json) {
        try {
            redisTemplate.opsForValue().set(WORKFLOW_PREFIX + key, json, WORKFLOW_TTL, TimeUnit.SECONDS);
            cacheWriteCounter.increment();
        } catch (Exception e) {
            log.warn("Failed to cache workflow {}: {}", key, e.getMessage());
        }
    }

    public Optional<String> getWorkflow(String key) {
        var cached = redisTemplate.opsForValue().get(WORKFLOW_PREFIX + key);
        if (cached != null) {
            cacheHitCounter.increment();
            return Optional.of(cached);
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void evictWorkflow(String key) {
        redisTemplate.delete(WORKFLOW_PREFIX + key);
    }

    public void cacheScheduler(String key, String json) {
        try {
            redisTemplate.opsForValue().set(SCHEDULER_PREFIX + key, json, SCHEDULER_TTL, TimeUnit.SECONDS);
            cacheWriteCounter.increment();
        } catch (Exception e) {
            log.warn("Failed to cache scheduler {}: {}", key, e.getMessage());
        }
    }

    public Optional<String> getScheduler(String key) {
        var cached = redisTemplate.opsForValue().get(SCHEDULER_PREFIX + key);
        if (cached != null) {
            cacheHitCounter.increment();
            return Optional.of(cached);
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void evictScheduler(String key) {
        redisTemplate.delete(SCHEDULER_PREFIX + key);
    }

    public void cacheLifecycle(String key, String json) {
        try {
            redisTemplate.opsForValue().set(LIFECYCLE_PREFIX + key, json, LIFECYCLE_TTL, TimeUnit.SECONDS);
            cacheWriteCounter.increment();
        } catch (Exception e) {
            log.warn("Failed to cache lifecycle {}: {}", key, e.getMessage());
        }
    }

    public Optional<String> getLifecycle(String key) {
        var cached = redisTemplate.opsForValue().get(LIFECYCLE_PREFIX + key);
        if (cached != null) {
            cacheHitCounter.increment();
            return Optional.of(cached);
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void evictLifecycle(String key) {
        redisTemplate.delete(LIFECYCLE_PREFIX + key);
    }

    public void cacheConfig(String key, String json) {
        try {
            redisTemplate.opsForValue().set(CONFIG_PREFIX + key, json, CONFIG_TTL, TimeUnit.SECONDS);
            cacheWriteCounter.increment();
        } catch (Exception e) {
            log.warn("Failed to cache config {}: {}", key, e.getMessage());
        }
    }

    public Optional<String> getConfig(String key) {
        var cached = redisTemplate.opsForValue().get(CONFIG_PREFIX + key);
        if (cached != null) {
            cacheHitCounter.increment();
            return Optional.of(cached);
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void evictConfig(String key) {
        redisTemplate.delete(CONFIG_PREFIX + key);
    }

    public void cacheStatistics(String key, String json) {
        try {
            redisTemplate.opsForValue().set(STATISTICS_PREFIX + key, json, STATISTICS_TTL, TimeUnit.SECONDS);
            cacheWriteCounter.increment();
        } catch (Exception e) {
            log.warn("Failed to cache statistics {}: {}", key, e.getMessage());
        }
    }

    public Optional<String> getStatistics(String key) {
        var cached = redisTemplate.opsForValue().get(STATISTICS_PREFIX + key);
        if (cached != null) {
            cacheHitCounter.increment();
            return Optional.of(cached);
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void evictStatistics(String key) {
        redisTemplate.delete(STATISTICS_PREFIX + key);
    }

    public void invalidateAll() {
        var keysToDelete = Stream.of(
                        WORKFLOW_PREFIX + "*",
                        SCHEDULER_PREFIX + "*",
                        LIFECYCLE_PREFIX + "*",
                        CONFIG_PREFIX + "*",
                        STATISTICS_PREFIX + "*"
                )
                .flatMap(pattern -> redisTemplate.keys(pattern).stream())
                .collect(Collectors.toSet());

        if (!keysToDelete.isEmpty()) {
            redisTemplate.delete(keysToDelete);
            log.info("Invalidated {} automation cache keys", keysToDelete.size());
        }
    }
}
