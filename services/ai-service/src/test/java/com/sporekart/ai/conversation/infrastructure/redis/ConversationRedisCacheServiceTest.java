package com.sporekart.ai.conversation.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConversationRedisCacheServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOps;

    private ConversationRedisCacheService cacheService;
    private SimpleMeterRegistry meterRegistry;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOps);
        cacheService = new ConversationRedisCacheService(redisTemplate, objectMapper, meterRegistry);
    }

    @Test
    void shouldCacheSession() {
        var sessionId = UUID.randomUUID();
        var sessionJson = "{\"id\":\"" + sessionId + "\",\"userId\":\"user-1\"}";

        cacheService.cacheSession(sessionId, sessionJson);

        verify(valueOps).set(eq("conversation:session:" + sessionId), eq(sessionJson), anyLong(), any());
    }

    @Test
    void shouldCacheAndRetrieveSession() {
        var sessionId = UUID.randomUUID();
        var sessionJson = "{\"id\":\"" + sessionId + "\",\"userId\":\"user-1\"}";
        when(valueOps.get("conversation:session:" + sessionId)).thenReturn(sessionJson);

        var result = cacheService.getCachedSession(sessionId);

        assertTrue(result.isPresent());
        assertEquals(sessionJson, result.get());
    }

    @Test
    void shouldReturnEmptyForCacheMiss() {
        var sessionId = UUID.randomUUID();
        when(valueOps.get("conversation:session:" + sessionId)).thenReturn(null);

        var result = cacheService.getCachedSession(sessionId);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldCacheMessages() {
        var sessionId = UUID.randomUUID();
        var messagesJson = "[{\"content\":\"Hello\"}]";

        cacheService.cacheMessages(sessionId, messagesJson);

        verify(valueOps).set(eq("conversation:message:" + sessionId), eq(messagesJson), anyLong(), any());
    }

    @Test
    void shouldReturnCachedMessages() {
        var sessionId = UUID.randomUUID();
        var messagesJson = "[{\"content\":\"Hello\"}]";
        when(valueOps.get("conversation:message:" + sessionId)).thenReturn(messagesJson);

        var result = cacheService.getCachedMessages(sessionId);

        assertTrue(result.isPresent());
        assertEquals(messagesJson, result.get());
    }

    @Test
    void shouldCacheSessionList() {
        var userId = "user-1";
        var sessionsJson = "[{\"userId\":\"user-1\"}]";

        cacheService.cacheSessionList(userId, sessionsJson);

        verify(valueOps).set(eq("conversation:sessions:" + userId), eq(sessionsJson), anyLong(), any());
    }

    @Test
    void shouldInvalidateSession() {
        var sessionId = UUID.randomUUID();

        cacheService.invalidateSession(sessionId);

        verify(redisTemplate).delete("conversation:session:" + sessionId);
        verify(redisTemplate).delete("conversation:message:" + sessionId);
        verify(redisTemplate).delete("conversation:context:" + sessionId);
    }

    @Test
    void shouldInvalidateSessionList() {
        cacheService.invalidateSessionList("user-1");

        verify(redisTemplate).delete("conversation:sessions:user-1");
    }

    @Test
    void shouldHandleCacheWriteFailure() {
        var sessionId = UUID.randomUUID();
        doThrow(new RuntimeException("Redis down"))
                .when(valueOps).set(anyString(), anyString(), anyLong(), any());

        cacheService.cacheSession(sessionId, "{}");

        verify(redisTemplate).opsForValue();
    }
}
