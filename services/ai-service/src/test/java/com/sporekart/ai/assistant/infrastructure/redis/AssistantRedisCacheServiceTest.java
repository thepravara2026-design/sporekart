package com.sporekart.ai.assistant.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sporekart.ai.assistant.domain.AssistantIntent;
import com.sporekart.ai.assistant.domain.AssistantProfile;
import com.sporekart.ai.assistant.domain.AssistantTask;
import com.sporekart.ai.assistant.domain.IntentPriority;
import com.sporekart.ai.assistant.domain.IntentStatus;
import com.sporekart.ai.assistant.domain.TaskStatus;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssistantRedisCacheServiceTest {

    @Mock private StringRedisTemplate redisTemplate;
    @Mock private ValueOperations<String, String> valueOps;

    private AssistantRedisCacheService cacheService;
    private SimpleMeterRegistry meterRegistry;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOps);
        cacheService = new AssistantRedisCacheService(redisTemplate, objectMapper, meterRegistry);
    }

    @Test
    void shouldCacheAndRetrieveAssistantProfile() {
        var assistantId = UUID.randomUUID();
        var profile = new AssistantProfile(UUID.randomUUID(), assistantId, "Test Assistant",
                "Hello!", List.of("search", "help"), Map.of(), Map.of(),
                true, OffsetDateTime.now(), OffsetDateTime.now());

        cacheService.cacheAssistantProfile(assistantId, profile);

        verify(valueOps).set(eq("assistant:profile:" + assistantId), anyString(), anyLong(), any());
        assertEquals(1.0, meterRegistry.counter("assistant.cache.writes").count());
    }

    @Test
    void shouldReturnCachedAssistantProfile() throws Exception {
        var assistantId = UUID.randomUUID();
        var profile = new AssistantProfile(UUID.randomUUID(), assistantId, "Test",
                "Hi", List.of(), Map.of(), Map.of(), true, OffsetDateTime.now(), OffsetDateTime.now());
        var json = objectMapper.writeValueAsString(profile);

        when(valueOps.get("assistant:profile:" + assistantId)).thenReturn(json);

        var result = cacheService.getAssistantProfile(assistantId);

        assertTrue(result.isPresent());
        assertEquals(assistantId, result.get().assistantId());
        assertEquals(1.0, meterRegistry.counter("assistant.cache.hits").count());
    }

    @Test
    void shouldReturnEmptyForCacheMissOnProfile() {
        var assistantId = UUID.randomUUID();
        when(valueOps.get("assistant:profile:" + assistantId)).thenReturn(null);

        var result = cacheService.getAssistantProfile(assistantId);

        assertTrue(result.isEmpty());
        assertEquals(1.0, meterRegistry.counter("assistant.cache.misses").count());
    }

    @Test
    void shouldCacheAndRetrieveIntent() {
        var sessionId = UUID.randomUUID();
        var intent = new AssistantIntent(UUID.randomUUID(), sessionId, "input",
                "product_search", 0.9, IntentStatus.RESOLVED, IntentPriority.HIGH,
                Map.of(), List.of(), null, OffsetDateTime.now(), OffsetDateTime.now());

        cacheService.cacheIntent(sessionId, intent);

        verify(valueOps).set(eq("assistant:intent:" + sessionId), anyString(), anyLong(), any());
    }

    @Test
    void shouldReturnCachedIntent() throws Exception {
        var sessionId = UUID.randomUUID();
        var intent = new AssistantIntent(UUID.randomUUID(), sessionId, "input",
                "product_search", 0.9, IntentStatus.RESOLVED, IntentPriority.HIGH,
                Map.of(), List.of(), null, OffsetDateTime.now(), OffsetDateTime.now());
        var json = objectMapper.writeValueAsString(intent);

        when(valueOps.get("assistant:intent:" + sessionId)).thenReturn(json);

        var result = cacheService.getIntent(sessionId);

        assertTrue(result.isPresent());
        assertEquals(sessionId, result.get().sessionId());
    }

    @Test
    void shouldReturnEmptyForCacheMissOnIntent() {
        var sessionId = UUID.randomUUID();
        when(valueOps.get("assistant:intent:" + sessionId)).thenReturn(null);

        var result = cacheService.getIntent(sessionId);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldCacheAndRetrieveSessionContext() {
        var sessionId = UUID.randomUUID();
        var context = Map.<String, Object>of("key", "value");

        cacheService.cacheSessionContext(sessionId, context);

        verify(valueOps).set(eq("assistant:session:context:" + sessionId), anyString(), anyLong(), any());
    }

    @Test
    void shouldReturnCachedSessionContext() {
        var sessionId = UUID.randomUUID();
        var json = "{\"key\":\"value\"}";
        when(valueOps.get("assistant:session:context:" + sessionId)).thenReturn(json);

        var result = cacheService.getSessionContext(sessionId);

        assertTrue(result.isPresent());
        assertEquals("value", result.get().get("key"));
    }

    @Test
    void shouldReturnEmptyForCacheMissOnSessionContext() {
        var sessionId = UUID.randomUUID();
        when(valueOps.get("assistant:session:context:" + sessionId)).thenReturn(null);

        var result = cacheService.getSessionContext(sessionId);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldCacheAndRetrieveRecommendation() {
        var sessionId = UUID.randomUUID();
        var recommendation = Map.<String, Object>of("product", "123");

        cacheService.cacheRecommendation(sessionId, recommendation);

        verify(valueOps).set(eq("assistant:recommendation:" + sessionId), anyString(), anyLong(), any());
    }

    @Test
    void shouldReturnCachedRecommendation() {
        var sessionId = UUID.randomUUID();
        var json = "{\"product\":\"123\"}";
        when(valueOps.get("assistant:recommendation:" + sessionId)).thenReturn(json);

        var result = cacheService.getRecommendation(sessionId);

        assertTrue(result.isPresent());
    }

    @Test
    void shouldReturnEmptyForCacheMissOnRecommendation() {
        var sessionId = UUID.randomUUID();
        when(valueOps.get("assistant:recommendation:" + sessionId)).thenReturn(null);

        var result = cacheService.getRecommendation(sessionId);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldCacheAndRetrieveTaskState() {
        var taskId = UUID.randomUUID();
        var task = new AssistantTask(taskId, UUID.randomUUID(), UUID.randomUUID(),
                "QueryProducts", "Search", TaskStatus.PLANNING, 1, Map.of(), null,
                List.of(), 0, 3, 5000, null, OffsetDateTime.now(), null, null);

        cacheService.cacheTaskState(taskId, task);

        verify(valueOps).set(eq("assistant:task:" + taskId), anyString(), anyLong(), any());
    }

    @Test
    void shouldReturnCachedTaskState() throws Exception {
        var taskId = UUID.randomUUID();
        var task = new AssistantTask(taskId, UUID.randomUUID(), UUID.randomUUID(),
                "QueryProducts", "Search", TaskStatus.PLANNING, 1, Map.of(), null,
                List.of(), 0, 3, 5000, null, OffsetDateTime.now(), null, null);
        var json = objectMapper.writeValueAsString(task);

        when(valueOps.get("assistant:task:" + taskId)).thenReturn(json);

        var result = cacheService.getTaskState(taskId);

        assertTrue(result.isPresent());
        assertEquals(taskId, result.get().id());
    }

    @Test
    void shouldReturnEmptyForCacheMissOnTaskState() {
        var taskId = UUID.randomUUID();
        when(valueOps.get("assistant:task:" + taskId)).thenReturn(null);

        var result = cacheService.getTaskState(taskId);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldEvictSessionCache() {
        var sessionId = UUID.randomUUID();

        cacheService.evictSessionCache(sessionId);

        verify(redisTemplate).delete(anySet());
    }
}
