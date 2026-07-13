package com.sporekart.ai.semantic.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sporekart.ai.semantic.api.ContextRetrievalService;
import com.sporekart.ai.semantic.domain.SemanticSearchResult;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingEntity;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmbeddingCacheServiceTest {

    @Mock private StringRedisTemplate redisTemplate;
    @Mock private ValueOperations<String, String> valueOps;

    private ObjectMapper objectMapper;
    private SemanticRedisCacheService cacheService;
    private SimpleMeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOps);
        cacheService = new SemanticRedisCacheService(redisTemplate, objectMapper, meterRegistry);
    }

    @Test
    void testCacheAndRetrieveEmbeddingMetadata() {
        UUID id = UUID.randomUUID();
        when(valueOps.get("semantic:emb:metadata:" + id)).thenReturn(
                "{\"id\":\"" + id + "\",\"content\":\"test\",\"status\":\"COMPLETED\"}");

        Optional<SemanticEmbeddingEntity> entity = cacheService.getCachedEmbeddingMetadata(id);

        assertTrue(entity.isPresent());
        assertEquals("test", entity.get().getContent());
    }

    @Test
    void testCacheEmbeddingMetadata() {
        UUID id = UUID.randomUUID();
        SemanticEmbeddingEntity entity = new SemanticEmbeddingEntity();
        entity.setId(id);
        entity.setContent("test");

        cacheService.cacheEmbeddingMetadata(id, entity);

        verify(valueOps).set(anyString(), anyString(), anyLong(), any());
    }

    @Test
    void testCacheAndRetrieveSearchResults() {
        String key = "test-query";
        when(valueOps.get("semantic:search:" + key.hashCode()))
                .thenReturn("[{\"documentId\":\"1\",\"content\":\"doc1\",\"score\":0.9,\"rank\":1,\"metadata\":{}}]");

        List<SemanticSearchResult> cached = cacheService.getCachedSearchResults(key);

        assertNotNull(cached);
        assertEquals("doc1", cached.get(0).content());
    }

    @Test
    void testCacheSearchResults() {
        List<SemanticSearchResult> results = List.of(
                new SemanticSearchResult("1", "doc1", 0.9, 1, Map.of()));
        cacheService.cacheSearchResults("key", results);
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
    void testInvalidateAll() {
        when(redisTemplate.keys("semantic:*")).thenReturn(Set.of("semantic:emb:1", "semantic:search:1"));

        cacheService.invalidateAll();

        verify(redisTemplate).delete(Set.of("semantic:emb:1", "semantic:search:1"));
    }

    @Test
    void testCacheAndRetrieveStatistics() {
        String key = "stats";
        when(valueOps.get("semantic:stats:" + key)).thenReturn("{\"count\":10.0}");

        Map<String, Double> stats = cacheService.getCachedStatistics(key);

        assertNotNull(stats);
        assertEquals(10.0, stats.get("count"));
    }

    @Test
    void testCacheStatistics() {
        Map<String, Double> data = Map.of("count", 5.0);
        cacheService.cacheStatistics("key", data);
        verify(valueOps).set(anyString(), anyString(), anyLong(), any());
    }

    @Test
    void testCacheContextResult() {
        ContextRetrievalService.ContextResult result =
                new ContextRetrievalService.ContextResult("q", List.of(), 0);
        cacheService.cacheContextResult("key", result);
        verify(valueOps).set(anyString(), anyString(), anyLong(), any());
    }

    @Test
    void testGetCachedContextResult() {
        when(valueOps.get("semantic:ctx:" + "key".hashCode()))
                .thenReturn("{\"query\":\"q\",\"results\":[],\"totalResults\":0}");

        ContextRetrievalService.ContextResult result = cacheService.getCachedContextResult("key");

        assertNotNull(result);
        assertEquals("q", result.query());
    }
}
