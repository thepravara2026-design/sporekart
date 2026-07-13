package com.sporekart.ai.automation.infrastructure.redis;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
class AutomationRedisCacheServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;
    @Mock
    private MeterRegistry meterRegistry;
    @Mock
    private ValueOperations<String, String> valueOps;
    @Mock
    private Counter counter;

    private AutomationRedisCacheService cacheService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        when(meterRegistry.counter(anyString(), anyString(), anyString())).thenReturn(counter);
        when(meterRegistry.counter(anyString())).thenReturn(counter);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        cacheService = new AutomationRedisCacheService(redisTemplate, objectMapper, meterRegistry);
    }

    @Test
    void cacheWorkflowShouldSetValue() {
        cacheService.cacheWorkflow("wf-key", "{\"status\":\"pending\"}");

        verify(valueOps).set(eq("automation:workflow:wf-key"), eq("{\"status\":\"pending\"}"), anyLong(), any());
        verify(counter, atLeastOnce()).increment();
    }

    @Test
    void getWorkflowShouldReturnCachedValue() {
        when(valueOps.get("automation:workflow:wf-key")).thenReturn("{\"status\":\"pending\"}");

        var result = cacheService.getWorkflow("wf-key");

        assertTrue(result.isPresent());
        assertEquals("{\"status\":\"pending\"}", result.get());
        verify(counter, atLeastOnce()).increment();
    }

    @Test
    void getWorkflowShouldReturnEmptyOnMiss() {
        when(valueOps.get("automation:workflow:wf-key")).thenReturn(null);

        var result = cacheService.getWorkflow("wf-key");

        assertTrue(result.isEmpty());
        verify(counter, atLeastOnce()).increment();
    }

    @Test
    void evictWorkflowShouldDelete() {
        cacheService.evictWorkflow("wf-key");
        verify(redisTemplate).delete("automation:workflow:wf-key");
    }

    @Test
    void invalidateAllShouldDeleteAllKeys() {
        var keys = Set.of("automation:workflow:1", "automation:scheduler:1");
        when(redisTemplate.keys(anyString())).thenReturn(keys);

        cacheService.invalidateAll();

        verify(redisTemplate, times(5)).keys(anyString());
        verify(redisTemplate).delete(anySet());
    }

    @Test
    void invalidateAllShouldHandleEmptyKeys() {
        when(redisTemplate.keys(anyString())).thenReturn(Set.of());
        cacheService.invalidateAll();
        verify(redisTemplate, times(5)).keys(anyString());
        verify(redisTemplate, never()).delete(anySet());
    }

    @Test
    void cacheSchedulerShouldSetValue() {
        cacheService.cacheScheduler("sched-key", "{\"active\":true}");
        verify(valueOps).set(eq("automation:scheduler:sched-key"), eq("{\"active\":true}"), anyLong(), any());
    }

    @Test
    void getSchedulerShouldReturnCachedValue() {
        when(valueOps.get("automation:scheduler:sched-key")).thenReturn("data");
        var result = cacheService.getScheduler("sched-key");
        assertTrue(result.isPresent());
        assertEquals("data", result.get());
    }

    @Test
    void cacheLifecycleShouldSetValue() {
        cacheService.cacheLifecycle("lc-key", "{\"state\":\"ACTIVE\"}");
        verify(valueOps).set(eq("automation:lifecycle:lc-key"), anyString(), anyLong(), any());
    }

    @Test
    void getLifecycleShouldReturnCachedValue() {
        when(valueOps.get("automation:lifecycle:lc-key")).thenReturn("data");
        var result = cacheService.getLifecycle("lc-key");
        assertTrue(result.isPresent());
    }

    @Test
    void cacheConfigShouldSetValue() {
        cacheService.cacheConfig("cfg-key", "{\"enabled\":true}");
        verify(valueOps).set(eq("automation:config:cfg-key"), anyString(), anyLong(), any());
    }

    @Test
    void getConfigShouldReturnCachedValue() {
        when(valueOps.get("automation:config:cfg-key")).thenReturn("data");
        var result = cacheService.getConfig("cfg-key");
        assertTrue(result.isPresent());
    }

    @Test
    void cacheStatisticsShouldSetValue() {
        cacheService.cacheStatistics("stats-key", "{\"count\":5}");
        verify(valueOps).set(eq("automation:statistics:stats-key"), anyString(), anyLong(), any());
    }

    @Test
    void getStatisticsShouldReturnCachedValue() {
        when(valueOps.get("automation:statistics:stats-key")).thenReturn("data");
        var result = cacheService.getStatistics("stats-key");
        assertTrue(result.isPresent());
    }

    @Test
    void cacheWorkflowShouldHandleException() {
        when(valueOps.set(anyString(), anyString(), anyLong(), any())).thenThrow(new RuntimeException("redis down"));
        assertDoesNotThrow(() -> cacheService.cacheWorkflow("key", "{}"));
    }
}
