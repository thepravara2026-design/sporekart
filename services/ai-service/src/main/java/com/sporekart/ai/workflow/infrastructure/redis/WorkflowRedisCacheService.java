package com.sporekart.ai.workflow.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Profile("!test")
@Service
public class WorkflowRedisCacheService {

    private static final Logger log = LoggerFactory.getLogger(WorkflowRedisCacheService.class);

    private static final String DEF_PREFIX = "workflow:def:";
    private static final String EXEC_PREFIX = "workflow:exec:";
    private static final String STATS_PREFIX = "workflow:stats:";
    private static final String STATE_PREFIX = "workflow:state:";

    private static final long DEF_TTL_MINUTES = 30;
    private static final long EXEC_TTL_MINUTES = 15;
    private static final long STATS_TTL_MINUTES = 5;
    private static final long STATE_TTL_MINUTES = 10;

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;
    private final Counter cacheWriteCounter;

    public WorkflowRedisCacheService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper,
                                     MeterRegistry meterRegistry) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cacheHitCounter = Counter.builder("workflow.cache.hits")
                .description("Workflow cache hit count").register(meterRegistry);
        this.cacheMissCounter = Counter.builder("workflow.cache.misses")
                .description("Workflow cache miss count").register(meterRegistry);
        this.cacheWriteCounter = Counter.builder("workflow.cache.writes")
                .description("Workflow cache write count").register(meterRegistry);
    }

    public void cacheDefinition(UUID workflowId, String json) {
        try {
            redisTemplate.opsForValue().set(DEF_PREFIX + workflowId, json, DEF_TTL_MINUTES, TimeUnit.MINUTES);
            cacheWriteCounter.increment();
        } catch (Exception e) {
            log.warn("Failed to cache workflow definition {}: {}", workflowId, e.getMessage());
        }
    }

    public java.util.Optional<String> getCachedDefinition(UUID workflowId) {
        var key = DEF_PREFIX + workflowId;
        var cached = redisTemplate.opsForValue().get(key);
        if (cached != null) { cacheHitCounter.increment(); return java.util.Optional.of(cached); }
        cacheMissCounter.increment();
        return java.util.Optional.empty();
    }

    public void cacheExecution(UUID executionId, String json) {
        try {
            redisTemplate.opsForValue().set(EXEC_PREFIX + executionId, json, EXEC_TTL_MINUTES, TimeUnit.MINUTES);
            cacheWriteCounter.increment();
        } catch (Exception e) {
            log.warn("Failed to cache execution {}: {}", executionId, e.getMessage());
        }
    }

    public java.util.Optional<String> getCachedExecution(UUID executionId) {
        var key = EXEC_PREFIX + executionId;
        var cached = redisTemplate.opsForValue().get(key);
        if (cached != null) { cacheHitCounter.increment(); return java.util.Optional.of(cached); }
        cacheMissCounter.increment();
        return java.util.Optional.empty();
    }

    public void cacheExecutionState(UUID executionId, String json) {
        try {
            redisTemplate.opsForValue().set(STATE_PREFIX + executionId, json, STATE_TTL_MINUTES, TimeUnit.MINUTES);
            cacheWriteCounter.increment();
        } catch (Exception e) {
            log.warn("Failed to cache state for execution {}: {}", executionId, e.getMessage());
        }
    }

    public java.util.Optional<String> getCachedExecutionState(UUID executionId) {
        var key = STATE_PREFIX + executionId;
        var cached = redisTemplate.opsForValue().get(key);
        if (cached != null) { cacheHitCounter.increment(); return java.util.Optional.of(cached); }
        cacheMissCounter.increment();
        return java.util.Optional.empty();
    }

    public void invalidateDefinition(UUID workflowId) {
        redisTemplate.delete(DEF_PREFIX + workflowId);
    }

    public void invalidateExecution(UUID executionId) {
        redisTemplate.delete(EXEC_PREFIX + executionId);
        redisTemplate.delete(STATE_PREFIX + executionId);
    }

    public void invalidateAll() {
        var keys = redisTemplate.keys("workflow:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("Invalidated {} workflow cache keys", keys.size());
        }
    }
}
