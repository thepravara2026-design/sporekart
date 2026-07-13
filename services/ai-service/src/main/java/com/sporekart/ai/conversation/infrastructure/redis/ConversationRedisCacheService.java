package com.sporekart.ai.conversation.infrastructure.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Profile("!test")
@Service
public class ConversationRedisCacheService {

    private static final Logger log = LoggerFactory.getLogger(ConversationRedisCacheService.class);

    private static final String SESSION_PREFIX = "conversation:session:";
    private static final String MESSAGE_PREFIX = "conversation:message:";
    private static final String MEMORY_PREFIX = "conversation:memory:";
    private static final String CONTEXT_PREFIX = "conversation:context:";
    private static final String SESSION_LIST_PREFIX = "conversation:sessions:";

    private static final long SESSION_TTL_MINUTES = 30;
    private static final long MESSAGE_TTL_MINUTES = 15;
    private static final long MEMORY_TTL_MINUTES = 60;
    private static final long CONTEXT_TTL_MINUTES = 10;

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;
    private final Counter cacheWriteCounter;

    public ConversationRedisCacheService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper,
                                         MeterRegistry meterRegistry) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cacheHitCounter = Counter.builder("conversation.cache.hits")
                .description("Conversation cache hit count").register(meterRegistry);
        this.cacheMissCounter = Counter.builder("conversation.cache.misses")
                .description("Conversation cache miss count").register(meterRegistry);
        this.cacheWriteCounter = Counter.builder("conversation.cache.writes")
                .description("Conversation cache write count").register(meterRegistry);
    }

    public void cacheSession(UUID sessionId, String sessionJson) {
        try {
            redisTemplate.opsForValue().set(SESSION_PREFIX + sessionId, sessionJson, SESSION_TTL_MINUTES, TimeUnit.MINUTES);
            cacheWriteCounter.increment();
        } catch (Exception e) {
            log.warn("Failed to cache session {}: {}", sessionId, e.getMessage());
        }
    }

    public Optional<String> getCachedSession(UUID sessionId) {
        var key = SESSION_PREFIX + sessionId;
        var cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            cacheHitCounter.increment();
            return Optional.of(cached);
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void cacheMessages(UUID sessionId, String messagesJson) {
        try {
            redisTemplate.opsForValue().set(MESSAGE_PREFIX + sessionId, messagesJson, MESSAGE_TTL_MINUTES, TimeUnit.MINUTES);
            cacheWriteCounter.increment();
        } catch (Exception e) {
            log.warn("Failed to cache messages for session {}: {}", sessionId, e.getMessage());
        }
    }

    public Optional<String> getCachedMessages(UUID sessionId) {
        var key = MESSAGE_PREFIX + sessionId;
        var cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            cacheHitCounter.increment();
            return Optional.of(cached);
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void cacheSessionList(String userId, String sessionsJson) {
        try {
            redisTemplate.opsForValue().set(SESSION_LIST_PREFIX + userId, sessionsJson, SESSION_TTL_MINUTES, TimeUnit.MINUTES);
            cacheWriteCounter.increment();
        } catch (Exception e) {
            log.warn("Failed to cache session list for user {}: {}", userId, e.getMessage());
        }
    }

    public Optional<String> getCachedSessionList(String userId) {
        var key = SESSION_LIST_PREFIX + userId;
        var cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            cacheHitCounter.increment();
            return Optional.of(cached);
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void invalidateSession(UUID sessionId) {
        redisTemplate.delete(SESSION_PREFIX + sessionId);
        redisTemplate.delete(MESSAGE_PREFIX + sessionId);
        redisTemplate.delete(CONTEXT_PREFIX + sessionId);
    }

    public void invalidateSessionList(String userId) {
        redisTemplate.delete(SESSION_LIST_PREFIX + userId);
    }

    public void invalidateAll() {
        var keys = redisTemplate.keys("conversation:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("Invalidated {} conversation cache keys", keys.size());
        }
    }
}
