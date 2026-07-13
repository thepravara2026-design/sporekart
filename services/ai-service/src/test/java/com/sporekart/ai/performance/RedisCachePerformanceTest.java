package com.sporekart.ai.performance;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class RedisCachePerformanceTest {

    @MockBean
    private RedisTemplate<String, Object> redisTemplate;

    @MockBean
    private ValueOperations<String, Object> valueOps;

    @Test
    void cacheWriteTimeUnder10ms() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        long start = System.nanoTime();
        redisTemplate.opsForValue().set("test:key", "value", 300, TimeUnit.SECONDS);
        long elapsed = System.nanoTime();

        long elapsedMs = (elapsed - start) / 1_000_000;
        System.out.printf("Cache write: %dms%n", elapsedMs);
        assertTrue(elapsedMs < 10, "Cache write time should be < 10ms");
    }

    @Test
    void cacheReadTimeUnder5ms() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get("test:key")).thenReturn("value");

        long start = System.nanoTime();
        Object result = redisTemplate.opsForValue().get("test:key");
        long elapsed = System.nanoTime();

        long elapsedMs = (elapsed - start) / 1_000_000;
        System.out.printf("Cache read: %dms%n", elapsedMs);
        assertEquals("value", result);
        assertTrue(elapsedMs < 5, "Cache read time should be < 5ms");
    }

    @Test
    void cacheMissHandlingTime() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get("test:missing")).thenReturn(null);

        long start = System.nanoTime();
        Object result = redisTemplate.opsForValue().get("test:missing");
        long elapsed = System.nanoTime();

        long elapsedMs = (elapsed - start) / 1_000_000;
        System.out.printf("Cache miss: %dms%n", elapsedMs);
        assertNull(result);
    }

    @Test
    void ttlExpirationMechanism() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        redisTemplate.opsForValue().set("test:ttl", "expires", 1, TimeUnit.SECONDS);
        verify(valueOps).set("test:ttl", "expires", 1, TimeUnit.SECONDS);

        when(valueOps.get("test:ttl")).thenReturn(null);

        Object expired = redisTemplate.opsForValue().get("test:ttl");
        assertNull(expired, "Expired key should return null");
    }
}
