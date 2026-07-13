package com.sporekart.ai.content.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Profile("!test")
@Service
public class ContentRedisCacheService {

    private static final Logger log = LoggerFactory.getLogger(ContentRedisCacheService.class);

    private static final String GEN_PREFIX = "content:gen:";
    private static final String SUMMARY_PREFIX = "content:summary:";
    private static final String TRANSLATION_PREFIX = "content:translation:";
    private static final String SEO_PREFIX = "content:seo:";
    private static final String TEMPLATE_PREFIX = "content:template:";

    private static final long GEN_TTL_MINUTES = 15;
    private static final long SUMMARY_TTL_MINUTES = 15;
    private static final long TRANSLATION_TTL_MINUTES = 15;
    private static final long SEO_TTL_MINUTES = 15;
    private static final long TEMPLATE_TTL_MINUTES = 30;

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;
    private final Counter cacheWriteCounter;

    public ContentRedisCacheService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper,
                                    MeterRegistry meterRegistry) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cacheHitCounter = Counter.builder("content.cache.hits")
                .description("Content cache hit count").register(meterRegistry);
        this.cacheMissCounter = Counter.builder("content.cache.misses")
                .description("Content cache miss count").register(meterRegistry);
        this.cacheWriteCounter = Counter.builder("content.cache.writes")
                .description("Content cache write count").register(meterRegistry);
    }

    public void cacheGeneration(UUID contentId, String json) {
        try {
            redisTemplate.opsForValue().set(GEN_PREFIX + contentId, json, GEN_TTL_MINUTES, TimeUnit.MINUTES);
            cacheWriteCounter.increment();
        } catch (Exception e) {
            log.warn("Failed to cache generation {}: {}", contentId, e.getMessage());
        }
    }

    public Optional<String> getCachedGeneration(UUID contentId) {
        var key = GEN_PREFIX + contentId;
        var cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            cacheHitCounter.increment();
            return Optional.of(cached);
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void cacheTemplate(UUID templateId, String json) {
        try {
            redisTemplate.opsForValue().set(TEMPLATE_PREFIX + templateId, json, TEMPLATE_TTL_MINUTES, TimeUnit.MINUTES);
            cacheWriteCounter.increment();
        } catch (Exception e) {
            log.warn("Failed to cache template {}: {}", templateId, e.getMessage());
        }
    }

    public Optional<String> getCachedTemplate(UUID templateId) {
        var key = TEMPLATE_PREFIX + templateId;
        var cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            cacheHitCounter.increment();
            return Optional.of(cached);
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void cacheSEO(UUID seoId, String json) {
        try {
            redisTemplate.opsForValue().set(SEO_PREFIX + seoId, json, SEO_TTL_MINUTES, TimeUnit.MINUTES);
            cacheWriteCounter.increment();
        } catch (Exception e) {
            log.warn("Failed to cache SEO {}: {}", seoId, e.getMessage());
        }
    }

    public Optional<String> getCachedSEO(UUID seoId) {
        var key = SEO_PREFIX + seoId;
        var cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            cacheHitCounter.increment();
            return Optional.of(cached);
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void invalidateGeneration(UUID contentId) {
        redisTemplate.delete(GEN_PREFIX + contentId);
        redisTemplate.delete(SUMMARY_PREFIX + contentId);
        redisTemplate.delete(TRANSLATION_PREFIX + contentId);
        redisTemplate.delete(SEO_PREFIX + contentId);
    }

    public void invalidateTemplate(UUID templateId) {
        redisTemplate.delete(TEMPLATE_PREFIX + templateId);
    }

    public void invalidateAll() {
        var keys = redisTemplate.keys("content:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("Invalidated {} content cache keys", keys.size());
        }
    }
}
