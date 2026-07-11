package com.sporekart.ai.prompt.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromptRedisCacheServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;

    private PromptRedisCacheService cacheService;

    @BeforeEach
    void setUp() {
        cacheService = new PromptRedisCacheService(redisTemplate, new ObjectMapper());
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void shouldCacheAndRetrievePrompt() {
        UUID id = UUID.randomUUID();
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(id);
        template.setName("cached");

        cacheService.cachePrompt(id, template);
        verify(valueOperations).set(anyString(), anyString(), eq(30L), eq(TimeUnit.MINUTES));
    }

    @Test
    void shouldFallbackWhenCacheMiss() {
        UUID id = UUID.randomUUID();
        when(valueOperations.get(anyString())).thenReturn(null);

        PromptTemplateEntity fallback = new PromptTemplateEntity();
        fallback.setId(id);
        fallback.setName("fallback");

        PromptTemplateEntity result = cacheService.getCachedPrompt(id, () -> fallback);
        assertEquals("fallback", result.getName());
    }

    @Test
    void shouldInvalidatePrompt() {
        UUID id = UUID.randomUUID();
        cacheService.invalidatePrompt(id);
        verify(redisTemplate, atLeastOnce()).delete(anyString());
    }

    @Test
    void shouldCachePublished() {
        UUID id = UUID.randomUUID();
        cacheService.cachePublished(id, "rendered text");
        verify(valueOperations).set(anyString(), eq("rendered text"), eq(15L), eq(TimeUnit.MINUTES));
    }

    @Test
    void shouldGetCachedPublishedWithFallback() {
        UUID id = UUID.randomUUID();
        when(valueOperations.get(anyString())).thenReturn(null);
        String result = cacheService.getCachedPublished(id, () -> "fallback text");
        assertEquals("fallback text", result);
    }
}
