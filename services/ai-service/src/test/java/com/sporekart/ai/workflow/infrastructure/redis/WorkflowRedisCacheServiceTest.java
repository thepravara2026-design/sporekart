package com.sporekart.ai.workflow.infrastructure.redis;

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
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkflowRedisCacheServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOps;

    private WorkflowRedisCacheService cacheService;
    private SimpleMeterRegistry meterRegistry;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOps);
        cacheService = new WorkflowRedisCacheService(redisTemplate, objectMapper, meterRegistry);
    }

    @Test
    void shouldCacheDefinition() {
        var workflowId = UUID.randomUUID();
        var json = "{\"id\":\"" + workflowId + "\",\"name\":\"Test\"}";

        cacheService.cacheDefinition(workflowId, json);

        verify(valueOps).set(eq("workflow:def:" + workflowId), eq(json), anyLong(), any());
    }

    @Test
    void shouldReturnCachedDefinition() {
        var workflowId = UUID.randomUUID();
        var json = "{\"id\":\"" + workflowId + "\",\"name\":\"Test\"}";
        when(valueOps.get("workflow:def:" + workflowId)).thenReturn(json);

        var result = cacheService.getCachedDefinition(workflowId);

        assertTrue(result.isPresent());
        assertEquals(json, result.get());
    }

    @Test
    void shouldReturnEmptyForCacheMissOnDefinition() {
        var workflowId = UUID.randomUUID();
        when(valueOps.get("workflow:def:" + workflowId)).thenReturn(null);

        var result = cacheService.getCachedDefinition(workflowId);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldCacheExecution() {
        var executionId = UUID.randomUUID();
        var json = "{\"id\":\"" + executionId + "\"}";

        cacheService.cacheExecution(executionId, json);

        verify(valueOps).set(eq("workflow:exec:" + executionId), eq(json), anyLong(), any());
    }

    @Test
    void shouldReturnCachedExecution() {
        var executionId = UUID.randomUUID();
        var json = "{\"id\":\"" + executionId + "\"}";
        when(valueOps.get("workflow:exec:" + executionId)).thenReturn(json);

        var result = cacheService.getCachedExecution(executionId);

        assertTrue(result.isPresent());
        assertEquals(json, result.get());
    }

    @Test
    void shouldReturnEmptyForCacheMissOnExecution() {
        var executionId = UUID.randomUUID();
        when(valueOps.get("workflow:exec:" + executionId)).thenReturn(null);

        var result = cacheService.getCachedExecution(executionId);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldCacheExecutionState() {
        var executionId = UUID.randomUUID();
        var json = "{\"status\":\"RUNNING\"}";

        cacheService.cacheExecutionState(executionId, json);

        verify(valueOps).set(eq("workflow:state:" + executionId), eq(json), anyLong(), any());
    }

    @Test
    void shouldReturnCachedExecutionState() {
        var executionId = UUID.randomUUID();
        var json = "{\"status\":\"RUNNING\"}";
        when(valueOps.get("workflow:state:" + executionId)).thenReturn(json);

        var result = cacheService.getCachedExecutionState(executionId);

        assertTrue(result.isPresent());
        assertEquals(json, result.get());
    }

    @Test
    void shouldReturnEmptyForCacheMissOnExecutionState() {
        var executionId = UUID.randomUUID();
        when(valueOps.get("workflow:state:" + executionId)).thenReturn(null);

        var result = cacheService.getCachedExecutionState(executionId);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldInvalidateDefinition() {
        var workflowId = UUID.randomUUID();

        cacheService.invalidateDefinition(workflowId);

        verify(redisTemplate).delete("workflow:def:" + workflowId);
    }

    @Test
    void shouldInvalidateExecution() {
        var executionId = UUID.randomUUID();

        cacheService.invalidateExecution(executionId);

        verify(redisTemplate).delete("workflow:exec:" + executionId);
        verify(redisTemplate).delete("workflow:state:" + executionId);
    }

    @Test
    void shouldInvalidateAll() {
        var keys = Set.of("workflow:def:key1", "workflow:exec:key2");
        when(redisTemplate.keys("workflow:*")).thenReturn(keys);

        cacheService.invalidateAll();

        verify(redisTemplate).delete(keys);
    }

    @Test
    void shouldHandleInvalidateAllWhenNoKeys() {
        when(redisTemplate.keys("workflow:*")).thenReturn(Set.of());

        cacheService.invalidateAll();

        verify(redisTemplate, never()).delete(anySet());
    }

    @Test
    void shouldHandleCacheWriteFailure() {
        var workflowId = UUID.randomUUID();
        doThrow(new RuntimeException("Redis down"))
                .when(valueOps).set(anyString(), anyString(), anyLong(), any());

        cacheService.cacheDefinition(workflowId, "{}");

        verify(redisTemplate).opsForValue();
    }
}
