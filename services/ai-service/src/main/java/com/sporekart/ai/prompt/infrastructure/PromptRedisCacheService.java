package com.sporekart.ai.prompt.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
public class PromptRedisCacheService {

    private static final Logger log = LoggerFactory.getLogger(PromptRedisCacheService.class);
    private static final String PROMPT_PREFIX = "prompt:";
    private static final String VERSION_PREFIX = "prompt:version:";
    private static final String CATEGORY_PREFIX = "prompt:cat:";
    private static final String METADATA_PREFIX = "prompt:meta:";
    private static final String PUBLISHED_PREFIX = "prompt:pub:";
    private static final long PROMPT_TTL_MINUTES = 30;
    private static final long VERSION_TTL_MINUTES = 30;
    private static final long CATEGORY_TTL_MINUTES = 60;
    private static final long METADATA_TTL_MINUTES = 10;
    private static final long PUBLISHED_TTL_MINUTES = 15;

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public PromptRedisCacheService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void cachePrompt(UUID id, PromptTemplateEntity template) {
        try {
            String key = PROMPT_PREFIX + id;
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(template),
                    PROMPT_TTL_MINUTES, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize prompt {} for caching", id);
        }
    }

    public PromptTemplateEntity getCachedPrompt(UUID id, Supplier<PromptTemplateEntity> fallback) {
        String key = PROMPT_PREFIX + id;
        String cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            try {
                return objectMapper.readValue(cached, PromptTemplateEntity.class);
            } catch (JsonProcessingException e) {
                log.warn("Failed to deserialize cached prompt {}", id);
            }
        }
        PromptTemplateEntity entity = fallback.get();
        if (entity != null) {
            cachePrompt(id, entity);
        }
        return entity;
    }

    public void cachePublished(UUID id, String renderedTemplate) {
        String key = PUBLISHED_PREFIX + id;
        redisTemplate.opsForValue().set(key, renderedTemplate, PUBLISHED_TTL_MINUTES, TimeUnit.MINUTES);
    }

    public String getCachedPublished(UUID id, Supplier<String> fallback) {
        String key = PUBLISHED_PREFIX + id;
        String cached = redisTemplate.opsForValue().get(key);
        if (cached != null) return cached;
        String rendered = fallback.get();
        if (rendered != null) {
            cachePublished(id, rendered);
        }
        return rendered;
    }

    public void invalidatePrompt(UUID id) {
        redisTemplate.delete(PROMPT_PREFIX + id);
        redisTemplate.delete(PUBLISHED_PREFIX + id);
        redisTemplate.delete(VERSION_PREFIX + id);
        redisTemplate.delete(METADATA_PREFIX + id);
        log.debug("Invalidated cache for prompt {}", id);
    }

    public void invalidateAll() {
        Set<String> keys = redisTemplate.keys(PROMPT_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
        keys = redisTemplate.keys(PUBLISHED_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
        keys = redisTemplate.keys(VERSION_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
        keys = redisTemplate.keys(CATEGORY_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
        log.debug("Invalidated all prompt caches");
    }
}
