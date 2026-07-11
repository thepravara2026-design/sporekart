package com.sporekart.ai.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AiCacheServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOps;

    @InjectMocks
    private AiCacheService cacheService;

    @Test
    void shouldReuseCachedValueForSameKey() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get("feature-flags")).thenReturn("enabled", "enabled");

        String first = cacheService.getOrPut("feature-flags", () -> "enabled");
        String second = cacheService.getOrPut("feature-flags", () -> "enabled");

        assertThat(first).isEqualTo("enabled");
        assertThat(second).isEqualTo("enabled");
        verify(valueOps, times(2)).get("feature-flags");
        verify(valueOps, never()).set(anyString(), anyString(), anyLong(), any());
    }
}
