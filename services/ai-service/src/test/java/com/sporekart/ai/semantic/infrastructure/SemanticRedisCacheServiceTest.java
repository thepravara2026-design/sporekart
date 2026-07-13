package com.sporekart.ai.semantic.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sporekart.ai.semantic.api.ContextRetrievalService;
import com.sporekart.ai.semantic.domain.SemanticSearchResult;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingEntity;
import io.micrometer.core.instrument.MeterRegistry;
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
class SemanticRedisCacheServiceTest {

    @Mock private StringRedisTemplate redisTemplate;
    @Mock private ValueOperations<String, String> valueOps;

    private final MeterRegistry meterRegistry = new SimpleMeterRegistry();
    private ObjectMapper objectMapper;
    private SemanticRedisCacheService cacheService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOps);
        cacheService = new SemanticRedisCacheService(redisTemplate, objectMapper, meterRegistry);
    }

    @Test
    void testCacheAndRetrieveEmbeddingMetadata() {
        UUID id = UUID.randomUUID();
        SemanticEmbeddingEntity entity = new SemanticEmbeddingEntity();
        entity.setId(id);
        entity.setContent("test");
        when(valueOps.get(eq("semantic:emb:metadata:" + id))).thenReturn(
                "{\"id\":\"" + id + "\",\"content\":\"test\",\"status\":\"COMPLETED\",\"version\":1}");

        Optional<SemanticEmbeddingEntity> cached = cacheService.getCachedEmbeddingMetadata(id);

        assertTrue(cached.isPresent());
        assertEquals("test", cached.get().getContent());
    }

    @Test
    void testCacheMissReturnsEmpty() {
        UUID id = UUID.randomUUID();
        when(valueOps.get(anyString())).thenReturn(null);

        Optional<SemanticEmbeddingEntity> cached = cacheService.getCachedEmbeddingMetadata(id);
        assertFalse(cached.isPresent());
    }

    @Test
    void testCacheAndRetrieveSearchResults() {
        when(valueOps.get(anyString())).thenReturn(
                "[{\"documentId\":\"1\",\"content\":\"test\",\"score\":0.9,\"rank\":1,\"metadata\":{}}]");

        List<SemanticSearchResult> results = cacheService.getCachedSearchResults("test-key");
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void testCacheSimilarityResults() {
        List<SemanticSearchResult> results = List.of(
                new SemanticSearchResult("1", "test", 0.9, 1, Map.of()));
        cacheService.cacheSimilarityResults("test-key", results);
        verify(valueOps).set(anyString(), anyString(), anyLong(), any());
    }

    @Test
    void testInvalidateEmbeddingMetadata() {
        UUID id = UUID.randomUUID();
        cacheService.invalidateEmbeddingMetadata(id);
        verify(redisTemplate).delete("semantic:emb:metadata:" + id);
        verify(redisTemplate).delete("semantic:emb:vector:" + id);
    }

    @Test
    void testCacheAndRetrieveStatistics() {
        when(valueOps.get(anyString())).thenReturn("{\"vector_count\":100.0}");
        Map<String, Double> stats = cacheService.getCachedStatistics("test-idx");
        assertNotNull(stats);
        assertEquals(100.0, stats.get("vector_count"));
    }

    @Test
    void testCacheContextResult() {
        var contextResult = new ContextRetrievalService.ContextResult("query", List.of(), 0);
        cacheService.cacheContextResult("test-key", contextResult);
        verify(valueOps).set(anyString(), anyString(), anyLong(), any());
    }

    @Test
    void testGetCachedContextResult() {
        when(valueOps.get(anyString())).thenReturn("{\"query\":\"test\",\"results\":[],\"totalResults\":0}");
        ContextRetrievalService.ContextResult result = cacheService.getCachedContextResult("test-key");
        assertNotNull(result);
        assertEquals("test", result.query());
    }

    @Test
    void testInvalidateAll() {
        when(redisTemplate.keys("semantic:*")).thenReturn(java.util.Set.of("semantic:key1", "semantic:key2"));
        cacheService.invalidateAll();
        verify(redisTemplate).delete(java.util.Set.of("semantic:key1", "semantic:key2"));
    }

    @Test
    void testInvalidateStatistics() {
        cacheService.invalidateStatistics("test-idx");
        verify(redisTemplate).delete("semantic:stats:test-idx");
    }
}
