package com.sporekart.ai.knowledge.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeDocumentEntity;
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
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KnowledgeRedisCacheServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;

    private KnowledgeRedisCacheService cacheService;

    @BeforeEach
    void setUp() {
        cacheService = new KnowledgeRedisCacheService(redisTemplate, new ObjectMapper(),
                new SimpleMeterRegistry());
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void shouldCacheAndRetrieveDocument() {
        UUID id = UUID.randomUUID();
        KnowledgeDocumentEntity doc = new KnowledgeDocumentEntity();
        doc.setId(id);
        doc.setTitle("cached doc");
        cacheService.cacheDocument(id, doc);
        verify(valueOperations).set(anyString(), anyString(), eq(30L), eq(TimeUnit.MINUTES));
    }

    @Test
    void shouldFallbackWhenCacheMiss() {
        UUID id = UUID.randomUUID();
        when(valueOperations.get(anyString())).thenReturn(null);

        KnowledgeDocumentEntity fallback = new KnowledgeDocumentEntity();
        fallback.setId(id);
        fallback.setTitle("fallback");

        KnowledgeDocumentEntity result = cacheService.getCachedDocument(id, () -> fallback);
        assertEquals("fallback", result.getTitle());
    }

    @Test
    void shouldCacheCategoryList() {
        cacheService.cacheCategoryList("[\"cat1\",\"cat2\"]");
        verify(valueOperations).set(eq("knowledge:cat:all"), eq("[\"cat1\",\"cat2\"]"), eq(60L), eq(TimeUnit.MINUTES));
    }

    @Test
    void shouldGetCachedCategoryList() {
        when(valueOperations.get("knowledge:cat:all")).thenReturn("[\"cat1\"]");
        Optional<String> cached = cacheService.getCachedCategoryList();
        assertTrue(cached.isPresent());
        assertEquals("[\"cat1\"]", cached.get());
    }

    @Test
    void shouldInvalidateDocument() {
        UUID id = UUID.randomUUID();
        cacheService.invalidateDocument(id);
        verify(redisTemplate, atLeastOnce()).keys(anyString());
    }

    @Test
    void shouldInvalidateAll() {
        when(redisTemplate.keys("knowledge:*")).thenReturn(java.util.Set.of("key1", "key2"));
        cacheService.invalidateAll();
        verify(redisTemplate).delete(java.util.Set.of("key1", "key2"));
    }
}
