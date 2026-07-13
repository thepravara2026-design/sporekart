package com.sporekart.ai.assistant.infrastructure.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.assistant.domain.AssistantIntent;
import com.sporekart.ai.assistant.domain.AssistantProfile;
import com.sporekart.ai.assistant.domain.AssistantTask;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Profile("!test")
@Service
public class AssistantRedisCacheService {

    private static final Logger log = LoggerFactory.getLogger(AssistantRedisCacheService.class);

    private static final String PROFILE_PREFIX = "assistant:profile:";
    private static final String INTENT_PREFIX = "assistant:intent:";
    private static final String SESSION_CONTEXT_PREFIX = "assistant:session:context:";
    private static final String RECOMMENDATION_PREFIX = "assistant:recommendation:";
    private static final String TASK_PREFIX = "assistant:task:";

    private static final long PROFILE_TTL_HOURS = 1;
    private static final long INTENT_TTL_MINUTES = 30;
    private static final long SESSION_CONTEXT_TTL_HOURS = 2;
    private static final long RECOMMENDATION_TTL_HOURS = 1;
    private static final long TASK_TTL_MINUTES = 30;

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;
    private final Counter cacheWriteCounter;

    public AssistantRedisCacheService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper,
                                      MeterRegistry meterRegistry) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cacheHitCounter = Counter.builder("assistant.cache.hits")
                .description("Assistant cache hit count").register(meterRegistry);
        this.cacheMissCounter = Counter.builder("assistant.cache.misses")
                .description("Assistant cache miss count").register(meterRegistry);
        this.cacheWriteCounter = Counter.builder("assistant.cache.writes")
                .description("Assistant cache write count").register(meterRegistry);
    }

    public void cacheAssistantProfile(UUID assistantId, AssistantProfile profile) {
        try {
            var json = objectMapper.writeValueAsString(profile);
            redisTemplate.opsForValue().set(PROFILE_PREFIX + assistantId, json,
                    PROFILE_TTL_HOURS, TimeUnit.HOURS);
            cacheWriteCounter.increment();
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize assistant profile {}: {}", assistantId, e.getMessage());
        } catch (Exception e) {
            log.warn("Failed to cache assistant profile {}: {}", assistantId, e.getMessage());
        }
    }

    public Optional<AssistantProfile> getAssistantProfile(UUID assistantId) {
        var key = PROFILE_PREFIX + assistantId;
        var cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            cacheHitCounter.increment();
            try {
                return Optional.of(objectMapper.readValue(cached, AssistantProfile.class));
            } catch (JsonProcessingException e) {
                log.warn("Failed to deserialize assistant profile {}: {}", assistantId, e.getMessage());
                cacheMissCounter.increment();
                return Optional.empty();
            }
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void cacheIntent(UUID sessionId, AssistantIntent intent) {
        try {
            var json = objectMapper.writeValueAsString(intent);
            redisTemplate.opsForValue().set(INTENT_PREFIX + sessionId, json,
                    INTENT_TTL_MINUTES, TimeUnit.MINUTES);
            cacheWriteCounter.increment();
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize intent for session {}: {}", sessionId, e.getMessage());
        } catch (Exception e) {
            log.warn("Failed to cache intent for session {}: {}", sessionId, e.getMessage());
        }
    }

    public Optional<AssistantIntent> getIntent(UUID sessionId) {
        var key = INTENT_PREFIX + sessionId;
        var cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            cacheHitCounter.increment();
            try {
                return Optional.of(objectMapper.readValue(cached, AssistantIntent.class));
            } catch (JsonProcessingException e) {
                log.warn("Failed to deserialize intent for session {}: {}", sessionId, e.getMessage());
                cacheMissCounter.increment();
                return Optional.empty();
            }
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public void cacheSessionContext(UUID sessionId, Map<String, Object> context) {
        try {
            var json = objectMapper.writeValueAsString(context);
            redisTemplate.opsForValue().set(SESSION_CONTEXT_PREFIX + sessionId, json,
                    SESSION_CONTEXT_TTL_HOURS, TimeUnit.HOURS);
            cacheWriteCounter.increment();
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize session context for {}: {}", sessionId, e.getMessage());
        } catch (Exception e) {
            log.warn("Failed to cache session context for {}: {}", sessionId, e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public Optional<Map<String, Object>> getSessionContext(UUID sessionId) {
        var key = SESSION_CONTEXT_PREFIX + sessionId;
        var cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            cacheHitCounter.increment();
            try {
                return Optional.of(objectMapper.readValue(cached, Map.class));
            } catch (JsonProcessingException e) {
                log.warn("Failed to deserialize session context for {}: {}", sessionId, e.getMessage());
                cacheMissCounter.increment();
                return Optional.empty();
            }
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void cacheRecommendation(UUID sessionId, Object recommendation) {
        try {
            var json = objectMapper.writeValueAsString(recommendation);
            redisTemplate.opsForValue().set(RECOMMENDATION_PREFIX + sessionId, json,
                    RECOMMENDATION_TTL_HOURS, TimeUnit.HOURS);
            cacheWriteCounter.increment();
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize recommendation for session {}: {}", sessionId, e.getMessage());
        } catch (Exception e) {
            log.warn("Failed to cache recommendation for session {}: {}", sessionId, e.getMessage());
        }
    }

    public Optional<Object> getRecommendation(UUID sessionId) {
        var key = RECOMMENDATION_PREFIX + sessionId;
        var cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            cacheHitCounter.increment();
            try {
                return Optional.of(objectMapper.readValue(cached, Object.class));
            } catch (JsonProcessingException e) {
                log.warn("Failed to deserialize recommendation for session {}: {}", sessionId, e.getMessage());
                cacheMissCounter.increment();
                return Optional.empty();
            }
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void cacheTaskState(UUID taskId, AssistantTask task) {
        try {
            var json = objectMapper.writeValueAsString(task);
            redisTemplate.opsForValue().set(TASK_PREFIX + taskId, json,
                    TASK_TTL_MINUTES, TimeUnit.MINUTES);
            cacheWriteCounter.increment();
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize task {}: {}", taskId, e.getMessage());
        } catch (Exception e) {
            log.warn("Failed to cache task {}: {}", taskId, e.getMessage());
        }
    }

    public Optional<AssistantTask> getTaskState(UUID taskId) {
        var key = TASK_PREFIX + taskId;
        var cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            cacheHitCounter.increment();
            try {
                return Optional.of(objectMapper.readValue(cached, AssistantTask.class));
            } catch (JsonProcessingException e) {
                log.warn("Failed to deserialize task {}: {}", taskId, e.getMessage());
                cacheMissCounter.increment();
                return Optional.empty();
            }
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void evictSessionCache(UUID sessionId) {
        var keysToDelete = Stream.of(
                        INTENT_PREFIX + sessionId,
                        SESSION_CONTEXT_PREFIX + sessionId,
                        RECOMMENDATION_PREFIX + sessionId
                )
                .collect(Collectors.toSet());

        if (!keysToDelete.isEmpty()) {
            redisTemplate.delete(keysToDelete);
            log.debug("Evicted {} cache keys for session {}", keysToDelete.size(), sessionId);
        }
    }
}
