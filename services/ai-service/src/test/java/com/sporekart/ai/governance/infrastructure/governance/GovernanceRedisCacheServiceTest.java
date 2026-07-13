package com.sporekart.ai.governance.infrastructure.governance;

import com.sporekart.ai.governance.config.GovernanceConfig;
import com.sporekart.ai.governance.infrastructure.redis.GovernanceRedisCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GovernanceRedisCacheServiceTest {

    @Mock private RedisTemplate<String, Object> redisTemplate;
    @Mock private ValueOperations<String, Object> valueOps;
    private GovernanceRedisCacheService cacheService;

    @BeforeEach
    void setUp() {
        GovernanceConfig config = new GovernanceConfig();
        cacheService = new GovernanceRedisCacheService(redisTemplate, config);
    }

    @Test
    void testCacheConfiguration() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        assertDoesNotThrow(() -> cacheService.cacheConfiguration("key1", "value1"));
        verify(valueOps).set(eq("gov:config:key1"), eq("value1"), anyLong(), any());
    }

    @Test
    void testGetConfiguration() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get("gov:config:key1")).thenReturn("value1");
        assertEquals("value1", cacheService.getConfiguration("key1"));
    }

    @Test
    void testCacheRegistry() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        assertDoesNotThrow(() -> cacheService.cacheRegistry("reg1", "data"));
        verify(valueOps).set(eq("gov:registry:reg1"), eq("data"), anyLong(), any());
    }

    @Test
    void testCacheHealth() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        assertDoesNotThrow(() -> cacheService.cacheHealth("health1", "ok"));
        verify(valueOps).set(eq("gov:health:health1"), eq("ok"), anyLong(), any());
    }

    @Test
    void testCacheMetrics() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        assertDoesNotThrow(() -> cacheService.cacheMetrics("metric1", 100));
        verify(valueOps).set(eq("gov:metrics:metric1"), eq(100), anyLong(), any());
    }

    @Test
    void testEvictConfiguration() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        cacheService.evictConfiguration("key1");
        verify(redisTemplate).delete("gov:config:key1");
    }
}
