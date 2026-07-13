package com.sporekart.ai.semantic.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.semantic.api.ContextRetrievalService;
import com.sporekart.ai.semantic.domain.SemanticSearchResult;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingEntity;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Profile("!test")
@Service
public class SemanticRedisCacheService {

    private static final Logger log = LoggerFactory.getLogger(SemanticRedisCacheService.class);

    private static final String EMB_META_PREFIX = "semantic:emb:metadata:";
    private static final String EMB_VECTOR_PREFIX = "semantic:emb:vector:";
    private static final String SEARCH_PREFIX = "semantic:search:";
    private static final String SIMILARITY_PREFIX = "semantic:sim:";
    private static final String INDEX_META_PREFIX = "semantic:idx:meta:";
    private static final String STATS_PREFIX = "semantic:stats:";
    private static final String CONTEXT_PREFIX = "semantic:ctx:";

    private static final long EMB_META_TTL_MINUTES = 30;
    private static final long EMB_VECTOR_TTL_MINUTES = 60;
    private static final long SEARCH_TTL_MINUTES = 5;
    private static final long SIMILARITY_TTL_MINUTES = 10;
    private static final long INDEX_META_TTL_MINUTES = 30;
    private static final long STATS_TTL_MINUTES = 5;
    private static final long CONTEXT_TTL_MINUTES = 10;

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;
    private final Counter cacheWriteCounter;

    public SemanticRedisCacheService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper,
                                     MeterRegistry meterRegistry) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cacheHitCounter = Counter.builder("semantic.cache.hits")
                .description("Semantic cache hit count").register(meterRegistry);
        this.cacheMissCounter = Counter.builder("semantic.cache.misses")
                .description("Semantic cache miss count").register(meterRegistry);
        this.cacheWriteCounter = Counter.builder("semantic.cache.writes")
                .description("Semantic cache write count").register(meterRegistry);
    }

    public void cacheEmbeddingMetadata(UUID id, SemanticEmbeddingEntity entity) {
        try {
            String json = objectMapper.writeValueAsString(entity);
            redisTemplate.opsForValue().set(EMB_META_PREFIX + id, json, EMB_META_TTL_MINUTES, TimeUnit.MINUTES);
            cacheWriteCounter.increment();
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize embedding metadata {}: {}", id, e.getMessage());
        }
    }

    public Optional<SemanticEmbeddingEntity> getCachedEmbeddingMetadata(UUID id) {
        String json = redisTemplate.opsForValue().get(EMB_META_PREFIX + id);
        if (json == null) {
            cacheMissCounter.increment();
            return Optional.empty();
        }
        try {
            cacheHitCounter.increment();
            return Optional.of(objectMapper.readValue(json, SemanticEmbeddingEntity.class));
        } catch (JsonProcessingException e) {
            cacheMissCounter.increment();
            log.warn("Failed to deserialize embedding metadata {}: {}", id, e.getMessage());
            return Optional.empty();
        }
    }

    public SemanticEmbeddingEntity getCachedEmbeddingMetadata(UUID id, Supplier<SemanticEmbeddingEntity> fallback) {
        return getCachedEmbeddingMetadata(id).orElseGet(() -> {
            SemanticEmbeddingEntity entity = fallback.get();
            if (entity != null) cacheEmbeddingMetadata(id, entity);
            return entity;
        });
    }

    public void cacheEmbeddingVector(String id, List<Double> vector) {
        try {
            String json = objectMapper.writeValueAsString(vector);
            redisTemplate.opsForValue().set(EMB_VECTOR_PREFIX + id, json, EMB_VECTOR_TTL_MINUTES, TimeUnit.MINUTES);
            cacheWriteCounter.increment();
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize embedding vector {}: {}", id, e.getMessage());
        }
    }

    public Optional<List<Double>> getCachedEmbeddingVector(String id) {
        String json = redisTemplate.opsForValue().get(EMB_VECTOR_PREFIX + id);
        if (json == null) {
            cacheMissCounter.increment();
            return Optional.empty();
        }
        try {
            cacheHitCounter.increment();
            return Optional.of(objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Double.class)));
        } catch (JsonProcessingException e) {
            cacheMissCounter.increment();
            log.warn("Failed to deserialize embedding vector {}: {}", id, e.getMessage());
            return Optional.empty();
        }
    }

    public void cacheSearchResults(String key, List<SemanticSearchResult> results) {
        try {
            String json = objectMapper.writeValueAsString(results);
            redisTemplate.opsForValue().set(SEARCH_PREFIX + key.hashCode(), json, SEARCH_TTL_MINUTES, TimeUnit.MINUTES);
            cacheWriteCounter.increment();
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize search results: {}", e.getMessage());
        }
    }

    public List<SemanticSearchResult> getCachedSearchResults(String key) {
        String json = redisTemplate.opsForValue().get(SEARCH_PREFIX + key.hashCode());
        if (json == null) {
            cacheMissCounter.increment();
            return null;
        }
        try {
            cacheHitCounter.increment();
            return objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, SemanticSearchResult.class));
        } catch (JsonProcessingException e) {
            cacheMissCounter.increment();
            log.warn("Failed to deserialize search results: {}", e.getMessage());
            return null;
        }
    }

    public void cacheSimilarityResults(String key, List<SemanticSearchResult> results) {
        try {
            String json = objectMapper.writeValueAsString(results);
            redisTemplate.opsForValue().set(SIMILARITY_PREFIX + key.hashCode(), json, SIMILARITY_TTL_MINUTES, TimeUnit.MINUTES);
            cacheWriteCounter.increment();
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize similarity results: {}", e.getMessage());
        }
    }

    public List<SemanticSearchResult> getCachedSimilarityResults(String key) {
        String json = redisTemplate.opsForValue().get(SIMILARITY_PREFIX + key.hashCode());
        if (json == null) {
            cacheMissCounter.increment();
            return null;
        }
        try {
            cacheHitCounter.increment();
            return objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, SemanticSearchResult.class));
        } catch (JsonProcessingException e) {
            cacheMissCounter.increment();
            log.warn("Failed to deserialize similarity results: {}", e.getMessage());
            return null;
        }
    }

    public void cacheIndexMetadata(String name, String json) {
        redisTemplate.opsForValue().set(INDEX_META_PREFIX + name, json, INDEX_META_TTL_MINUTES, TimeUnit.MINUTES);
        cacheWriteCounter.increment();
    }

    public Optional<String> getCachedIndexMetadata(String name) {
        String result = redisTemplate.opsForValue().get(INDEX_META_PREFIX + name);
        if (result != null) cacheHitCounter.increment();
        else cacheMissCounter.increment();
        return Optional.ofNullable(result);
    }

    public void cacheStatistics(String name, Map<String, Double> statistics) {
        try {
            String json = objectMapper.writeValueAsString(statistics);
            redisTemplate.opsForValue().set(STATS_PREFIX + name, json, STATS_TTL_MINUTES, TimeUnit.MINUTES);
            cacheWriteCounter.increment();
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize statistics: {}", e.getMessage());
        }
    }

    public Map<String, Double> getCachedStatistics(String name) {
        String json = redisTemplate.opsForValue().get(STATS_PREFIX + name);
        if (json == null) {
            cacheMissCounter.increment();
            return null;
        }
        try {
            cacheHitCounter.increment();
            return objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Double.class));
        } catch (JsonProcessingException e) {
            cacheMissCounter.increment();
            log.warn("Failed to deserialize statistics: {}", e.getMessage());
            return null;
        }
    }

    public void cacheContextResult(String key, ContextRetrievalService.ContextResult result) {
        try {
            String json = objectMapper.writeValueAsString(result);
            redisTemplate.opsForValue().set(CONTEXT_PREFIX + key.hashCode(), json, CONTEXT_TTL_MINUTES, TimeUnit.MINUTES);
            cacheWriteCounter.increment();
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize context result: {}", e.getMessage());
        }
    }

    public ContextRetrievalService.ContextResult getCachedContextResult(String key) {
        String json = redisTemplate.opsForValue().get(CONTEXT_PREFIX + key.hashCode());
        if (json == null) {
            cacheMissCounter.increment();
            return null;
        }
        try {
            cacheHitCounter.increment();
            return objectMapper.readValue(json, ContextRetrievalService.ContextResult.class);
        } catch (JsonProcessingException e) {
            cacheMissCounter.increment();
            log.warn("Failed to deserialize context result: {}", e.getMessage());
            return null;
        }
    }

    public void invalidateEmbeddingMetadata(UUID id) {
        redisTemplate.delete(EMB_META_PREFIX + id);
        redisTemplate.delete(EMB_VECTOR_PREFIX + id);
    }

    public void invalidateIndexMetadata(String name) {
        redisTemplate.delete(INDEX_META_PREFIX + name);
        redisTemplate.delete(STATS_PREFIX + name);
    }

    public void invalidateStatistics(String name) {
        redisTemplate.delete(STATS_PREFIX + name);
    }

    public void invalidateAll() {
        var keys = redisTemplate.keys("semantic:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
        log.debug("Invalidated all semantic cache entries");
    }
}
