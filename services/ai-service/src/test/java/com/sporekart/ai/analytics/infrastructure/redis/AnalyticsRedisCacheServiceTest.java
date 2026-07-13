package com.sporekart.ai.analytics.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsRedisCacheServiceTest {

    @Mock private StringRedisTemplate redisTemplate;
    @Mock private ValueOperations<String, String> valueOps;
    @Mock private MeterRegistry meterRegistry;
    @Mock private Counter counter;

    private AnalyticsRedisCacheService cacheService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        when(meterRegistry.counter(anyString(), anyString(), anyString())).thenReturn(counter);
        when(meterRegistry.counter(anyString())).thenReturn(counter);
        when(counter.increment()).thenReturn(1.0);

        cacheService = new AnalyticsRedisCacheService(redisTemplate, objectMapper, meterRegistry);
    }

    @Test
    void testCacheDashboard() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        cacheService.cacheDashboard("key1", "{\"data\":\"value\"}");

        verify(valueOps).set(eq("analytics:dashboard:key1"), eq("{\"data\":\"value\"}"), anyLong(), eq(TimeUnit.SECONDS));
    }

    @Test
    void testGetDashboard() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get("analytics:dashboard:key1")).thenReturn("cached-value");

        Optional<String> result = cacheService.getDashboard("key1");

        assertTrue(result.isPresent());
        assertEquals("cached-value", result.get());
    }

    @Test
    void testGetDashboardMiss() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get("analytics:dashboard:key1")).thenReturn(null);

        Optional<String> result = cacheService.getDashboard("key1");

        assertFalse(result.isPresent());
    }

    @Test
    void testInvalidateAll() {
        when(redisTemplate.keys(anyString())).thenReturn(Set.of("analytics:dashboard:k1", "analytics:metrics:k2"));

        cacheService.invalidateAll();

        verify(redisTemplate, atLeastOnce()).keys(anyString());
    }
}
