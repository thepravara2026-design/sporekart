package com.sporekart.ai.knowledge.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeDocumentEntity;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
public class KnowledgeRedisCacheService {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeRedisCacheService.class);
    private static final String DOC_PREFIX = "knowledge:doc:";
    private static final String CATEGORY_PREFIX = "knowledge:cat:";
    private static final String METADATA_PREFIX = "knowledge:meta:";
    private static final String SEARCH_PREFIX = "knowledge:search:";
    private static final String CITATION_PREFIX = "knowledge:cite:";
    private static final String RETRIEVAL_PREFIX = "knowledge:retrieve:";
    private static final long DOC_TTL_MINUTES = 30;
    private static final long CATEGORY_TTL_MINUTES = 60;
    private static final long METADATA_TTL_MINUTES = 10;
    private static final long SEARCH_TTL_MINUTES = 5;
    private static final long CITATION_TTL_MINUTES = 15;
    private static final long RETRIEVAL_TTL_MINUTES = 10;

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;
    private final Counter cacheWriteCounter;

    public KnowledgeRedisCacheService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper,
                                      MeterRegistry meterRegistry) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cacheHitCounter = Counter.builder("knowledge.cache.hits")
                .description("Knowledge cache hit count").register(meterRegistry);
        this.cacheMissCounter = Counter.builder("knowledge.cache.misses")
                .description("Knowledge cache miss count").register(meterRegistry);
        this.cacheWriteCounter = Counter.builder("knowledge.cache.writes")
                .description("Knowledge cache write count").register(meterRegistry);
    }

    public void cacheDocument(UUID id, KnowledgeDocumentEntity doc) {
        try {
            String json = objectMapper.writeValueAsString(doc);
            redisTemplate.opsForValue().set(DOC_PREFIX + id, json, DOC_TTL_MINUTES, TimeUnit.MINUTES);
            cacheWriteCounter.increment();
            log.debug("Cached knowledge document {} for {} min", id, DOC_TTL_MINUTES);
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize document {}: {}", id, e.getMessage());
        }
    }

    public Optional<KnowledgeDocumentEntity> getCachedDocument(UUID id) {
        String json = redisTemplate.opsForValue().get(DOC_PREFIX + id);
        if (json == null) {
            cacheMissCounter.increment();
            return Optional.empty();
        }
        try {
            cacheHitCounter.increment();
            return Optional.of(objectMapper.readValue(json, KnowledgeDocumentEntity.class));
        } catch (JsonProcessingException e) {
            cacheMissCounter.increment();
            log.warn("Failed to deserialize document {}: {}", id, e.getMessage());
            return Optional.empty();
        }
    }

    public KnowledgeDocumentEntity getCachedDocument(UUID id, Supplier<KnowledgeDocumentEntity> fallback) {
        return getCachedDocument(id).orElseGet(() -> {
            KnowledgeDocumentEntity doc = fallback.get();
            if (doc != null) cacheDocument(id, doc);
            return doc;
        });
    }

    public void cacheCategoryList(String json) {
        redisTemplate.opsForValue().set(CATEGORY_PREFIX + "all", json, CATEGORY_TTL_MINUTES, TimeUnit.MINUTES);
        cacheWriteCounter.increment();
    }

    public Optional<String> getCachedCategoryList() {
        String result = redisTemplate.opsForValue().get(CATEGORY_PREFIX + "all");
        if (result != null) cacheHitCounter.increment();
        else cacheMissCounter.increment();
        return Optional.ofNullable(result);
    }

    public void cacheMetadata(UUID documentId, String json) {
        redisTemplate.opsForValue().set(METADATA_PREFIX + documentId, json, METADATA_TTL_MINUTES, TimeUnit.MINUTES);
        cacheWriteCounter.increment();
    }

    public Optional<String> getCachedMetadata(UUID documentId) {
        String result = redisTemplate.opsForValue().get(METADATA_PREFIX + documentId);
        if (result != null) cacheHitCounter.increment();
        else cacheMissCounter.increment();
        return Optional.ofNullable(result);
    }

    public void cacheSearchResults(String query, String json) {
        redisTemplate.opsForValue().set(SEARCH_PREFIX + query.hashCode(), json, SEARCH_TTL_MINUTES, TimeUnit.MINUTES);
        cacheWriteCounter.increment();
    }

    public Optional<String> getCachedSearchResults(String query) {
        String result = redisTemplate.opsForValue().get(SEARCH_PREFIX + query.hashCode());
        if (result != null) cacheHitCounter.increment();
        else cacheMissCounter.increment();
        return Optional.ofNullable(result);
    }

    public void cacheRetrievalResults(UUID requestId, String json) {
        redisTemplate.opsForValue().set(RETRIEVAL_PREFIX + requestId, json, RETRIEVAL_TTL_MINUTES, TimeUnit.MINUTES);
        cacheWriteCounter.increment();
    }

    public Optional<String> getCachedRetrievalResults(UUID requestId) {
        String result = redisTemplate.opsForValue().get(RETRIEVAL_PREFIX + requestId);
        if (result != null) cacheHitCounter.increment();
        else cacheMissCounter.increment();
        return Optional.ofNullable(result);
    }

    public void cacheCitations(UUID documentId, String json) {
        redisTemplate.opsForValue().set(CITATION_PREFIX + documentId, json, CITATION_TTL_MINUTES, TimeUnit.MINUTES);
        cacheWriteCounter.increment();
    }

    public void invalidateDocument(UUID id) {
        String prefix = DOC_PREFIX + id;
        Set<String> keys = redisTemplate.keys(prefix + "*");
        if (keys != null && !keys.isEmpty()) redisTemplate.delete(keys);
        log.debug("Invalidated cache for knowledge document {}", id);
    }

    public void invalidateCategories() {
        redisTemplate.delete(CATEGORY_PREFIX + "all");
    }

    public void invalidateAll() {
        Set<String> keys = redisTemplate.keys("knowledge:*");
        if (keys != null && !keys.isEmpty()) redisTemplate.delete(keys);
        log.debug("Invalidated all knowledge cache entries");
    }
}
