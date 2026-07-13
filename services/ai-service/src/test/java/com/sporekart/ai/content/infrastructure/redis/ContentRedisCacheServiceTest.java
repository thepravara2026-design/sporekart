package com.sporekart.ai.content.infrastructure.redis;

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
class ContentRedisCacheServiceTest {

    @Mock private StringRedisTemplate redisTemplate;
    @Mock private ValueOperations<String, String> valueOps;

    private ContentRedisCacheService cacheService;
    private SimpleMeterRegistry meterRegistry;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOps);
        cacheService = new ContentRedisCacheService(redisTemplate, objectMapper, meterRegistry);
    }

    @Test
    void shouldCacheGeneration() {
        var contentId = UUID.randomUUID();
        var json = "{\"id\":\"" + contentId + "\",\"content\":\"test\"}";

        cacheService.cacheGeneration(contentId, json);

        verify(valueOps).set(eq("content:gen:" + contentId), eq(json), anyLong(), any());
        assertEquals(1.0, meterRegistry.counter("content.cache.writes").count());
    }

    @Test
    void shouldReturnCachedGeneration() {
        var contentId = UUID.randomUUID();
        var json = "{\"id\":\"" + contentId + "\"}";
        when(valueOps.get("content:gen:" + contentId)).thenReturn(json);

        var result = cacheService.getCachedGeneration(contentId);

        assertTrue(result.isPresent());
        assertEquals(json, result.get());
        assertEquals(1.0, meterRegistry.counter("content.cache.hits").count());
    }

    @Test
    void shouldReturnEmptyForCacheMissOnGeneration() {
        var contentId = UUID.randomUUID();
        when(valueOps.get("content:gen:" + contentId)).thenReturn(null);

        var result = cacheService.getCachedGeneration(contentId);

        assertTrue(result.isEmpty());
        assertEquals(1.0, meterRegistry.counter("content.cache.misses").count());
    }

    @Test
    void shouldCacheTemplate() {
        var templateId = UUID.randomUUID();
        var json = "{\"id\":\"" + templateId + "\",\"name\":\"Test\"}";

        cacheService.cacheTemplate(templateId, json);

        verify(valueOps).set(eq("content:template:" + templateId), eq(json), anyLong(), any());
    }

    @Test
    void shouldReturnCachedTemplate() {
        var templateId = UUID.randomUUID();
        var json = "{\"id\":\"" + templateId + "\"}";
        when(valueOps.get("content:template:" + templateId)).thenReturn(json);

        var result = cacheService.getCachedTemplate(templateId);

        assertTrue(result.isPresent());
        assertEquals(json, result.get());
    }

    @Test
    void shouldReturnEmptyForCacheMissOnTemplate() {
        var templateId = UUID.randomUUID();
        when(valueOps.get("content:template:" + templateId)).thenReturn(null);

        var result = cacheService.getCachedTemplate(templateId);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldCacheSEO() {
        var seoId = UUID.randomUUID();
        var json = "{\"id\":\"" + seoId + "\",\"seoScore\":75.0}";

        cacheService.cacheSEO(seoId, json);

        verify(valueOps).set(eq("content:seo:" + seoId), eq(json), anyLong(), any());
    }

    @Test
    void shouldReturnCachedSEO() {
        var seoId = UUID.randomUUID();
        var json = "{\"id\":\"" + seoId + "\"}";
        when(valueOps.get("content:seo:" + seoId)).thenReturn(json);

        var result = cacheService.getCachedSEO(seoId);

        assertTrue(result.isPresent());
        assertEquals(json, result.get());
    }

    @Test
    void shouldReturnEmptyForCacheMissOnSEO() {
        var seoId = UUID.randomUUID();
        when(valueOps.get("content:seo:" + seoId)).thenReturn(null);

        var result = cacheService.getCachedSEO(seoId);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldInvalidateGeneration() {
        var contentId = UUID.randomUUID();

        cacheService.invalidateGeneration(contentId);

        verify(redisTemplate).delete("content:gen:" + contentId);
        verify(redisTemplate).delete("content:summary:" + contentId);
        verify(redisTemplate).delete("content:translation:" + contentId);
        verify(redisTemplate).delete("content:seo:" + contentId);
    }

    @Test
    void shouldInvalidateTemplate() {
        var templateId = UUID.randomUUID();

        cacheService.invalidateTemplate(templateId);

        verify(redisTemplate).delete("content:template:" + templateId);
    }

    @Test
    void shouldInvalidateAll() {
        var keys = Set.of("content:gen:key1", "content:template:key2");
        when(redisTemplate.keys("content:*")).thenReturn(keys);

        cacheService.invalidateAll();

        verify(redisTemplate).delete(keys);
    }

    @Test
    void shouldHandleInvalidateAllWhenNoKeys() {
        when(redisTemplate.keys("content:*")).thenReturn(Set.of());

        cacheService.invalidateAll();

        verify(redisTemplate, never()).delete(anySet());
    }

    @Test
    void shouldHandleCacheWriteFailure() {
        var contentId = UUID.randomUUID();
        doThrow(new RuntimeException("Redis down"))
                .when(valueOps).set(anyString(), anyString(), anyLong(), any());

        cacheService.cacheGeneration(contentId, "{}");

        verify(redisTemplate).opsForValue();
    }

    @Test
    void shouldHandleInvalidateAllWhenKeysNull() {
        when(redisTemplate.keys("content:*")).thenReturn(null);

        cacheService.invalidateAll();

        verify(redisTemplate, never()).delete(anySet());
    }
}
