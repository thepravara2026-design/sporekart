package com.sporekart.ai.admin.infrastructure.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.admin.domain.AdminConfiguration;
import com.sporekart.ai.admin.domain.ConfigurationSnapshot;
import com.sporekart.ai.admin.domain.EnvironmentProfile;
import com.sporekart.ai.admin.domain.FeatureFlag;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Profile("!test")
@Slf4j
@Service
public class AdminRedisCacheService {

    private static final String CONFIGURATION_PREFIX = "admin:configuration:";
    private static final String FEATURE_FLAG_PREFIX = "admin:featureFlag:";
    private static final String ENVIRONMENT_PREFIX = "admin:environment:";
    private static final String SNAPSHOT_PREFIX = "admin:snapshot:";
    private static final String METADATA_PREFIX = "admin:metadata:";

    private static final long CONFIGURATION_TTL_SECONDS = 300;
    private static final long FEATURE_FLAG_TTL_SECONDS = 300;
    private static final long ENVIRONMENT_TTL_SECONDS = 600;
    private static final long SNAPSHOT_TTL_SECONDS = 600;
    private static final long METADATA_TTL_SECONDS = 300;

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;
    private final Counter cacheWriteCounter;

    public AdminRedisCacheService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper,
                                  MeterRegistry meterRegistry) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cacheHitCounter = Counter.builder("admin.cache.hits")
                .description("Admin cache hit count").register(meterRegistry);
        this.cacheMissCounter = Counter.builder("admin.cache.misses")
                .description("Admin cache miss count").register(meterRegistry);
        this.cacheWriteCounter = Counter.builder("admin.cache.writes")
                .description("Admin cache write count").register(meterRegistry);
    }

    public void cacheConfiguration(String cacheKey, AdminConfiguration configuration) {
        try {
            var json = objectMapper.writeValueAsString(configuration);
            redisTemplate.opsForValue().set(CONFIGURATION_PREFIX + cacheKey, json,
                    CONFIGURATION_TTL_SECONDS, TimeUnit.SECONDS);
            cacheWriteCounter.increment();
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize configuration {}: {}", cacheKey, e.getMessage());
        } catch (Exception e) {
            log.warn("Failed to cache configuration {}: {}", cacheKey, e.getMessage());
        }
    }

    public Optional<AdminConfiguration> getConfiguration(String cacheKey) {
        var key = CONFIGURATION_PREFIX + cacheKey;
        var cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            cacheHitCounter.increment();
            try {
                return Optional.of(objectMapper.readValue(cached, AdminConfiguration.class));
            } catch (JsonProcessingException e) {
                log.warn("Failed to deserialize configuration {}: {}", cacheKey, e.getMessage());
                cacheMissCounter.increment();
                return Optional.empty();
            }
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void evictConfiguration(String cacheKey) {
        redisTemplate.delete(CONFIGURATION_PREFIX + cacheKey);
    }

    public void cacheFeatureFlag(String cacheKey, FeatureFlag featureFlag) {
        try {
            var json = objectMapper.writeValueAsString(featureFlag);
            redisTemplate.opsForValue().set(FEATURE_FLAG_PREFIX + cacheKey, json,
                    FEATURE_FLAG_TTL_SECONDS, TimeUnit.SECONDS);
            cacheWriteCounter.increment();
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize feature flag {}: {}", cacheKey, e.getMessage());
        } catch (Exception e) {
            log.warn("Failed to cache feature flag {}: {}", cacheKey, e.getMessage());
        }
    }

    public Optional<FeatureFlag> getFeatureFlag(String cacheKey) {
        var key = FEATURE_FLAG_PREFIX + cacheKey;
        var cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            cacheHitCounter.increment();
            try {
                return Optional.of(objectMapper.readValue(cached, FeatureFlag.class));
            } catch (JsonProcessingException e) {
                log.warn("Failed to deserialize feature flag {}: {}", cacheKey, e.getMessage());
                cacheMissCounter.increment();
                return Optional.empty();
            }
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void evictFeatureFlag(String cacheKey) {
        redisTemplate.delete(FEATURE_FLAG_PREFIX + cacheKey);
    }

    public void cacheEnvironment(String cacheKey, EnvironmentProfile profile) {
        try {
            var json = objectMapper.writeValueAsString(profile);
            redisTemplate.opsForValue().set(ENVIRONMENT_PREFIX + cacheKey, json,
                    ENVIRONMENT_TTL_SECONDS, TimeUnit.SECONDS);
            cacheWriteCounter.increment();
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize environment {}: {}", cacheKey, e.getMessage());
        } catch (Exception e) {
            log.warn("Failed to cache environment {}: {}", cacheKey, e.getMessage());
        }
    }

    public Optional<EnvironmentProfile> getEnvironment(String cacheKey) {
        var key = ENVIRONMENT_PREFIX + cacheKey;
        var cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            cacheHitCounter.increment();
            try {
                return Optional.of(objectMapper.readValue(cached, EnvironmentProfile.class));
            } catch (JsonProcessingException e) {
                log.warn("Failed to deserialize environment {}: {}", cacheKey, e.getMessage());
                cacheMissCounter.increment();
                return Optional.empty();
            }
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void evictEnvironment(String cacheKey) {
        redisTemplate.delete(ENVIRONMENT_PREFIX + cacheKey);
    }

    public void cacheSnapshot(String cacheKey, ConfigurationSnapshot snapshot) {
        try {
            var json = objectMapper.writeValueAsString(snapshot);
            redisTemplate.opsForValue().set(SNAPSHOT_PREFIX + cacheKey, json,
                    SNAPSHOT_TTL_SECONDS, TimeUnit.SECONDS);
            cacheWriteCounter.increment();
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize snapshot {}: {}", cacheKey, e.getMessage());
        } catch (Exception e) {
            log.warn("Failed to cache snapshot {}: {}", cacheKey, e.getMessage());
        }
    }

    public Optional<ConfigurationSnapshot> getSnapshot(String cacheKey) {
        var key = SNAPSHOT_PREFIX + cacheKey;
        var cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            cacheHitCounter.increment();
            try {
                return Optional.of(objectMapper.readValue(cached, ConfigurationSnapshot.class));
            } catch (JsonProcessingException e) {
                log.warn("Failed to deserialize snapshot {}: {}", cacheKey, e.getMessage());
                cacheMissCounter.increment();
                return Optional.empty();
            }
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void evictSnapshot(String cacheKey) {
        redisTemplate.delete(SNAPSHOT_PREFIX + cacheKey);
    }

    public void cacheMetadata(String cacheKey, Object metadata) {
        try {
            var json = objectMapper.writeValueAsString(metadata);
            redisTemplate.opsForValue().set(METADATA_PREFIX + cacheKey, json,
                    METADATA_TTL_SECONDS, TimeUnit.SECONDS);
            cacheWriteCounter.increment();
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize metadata {}: {}", cacheKey, e.getMessage());
        } catch (Exception e) {
            log.warn("Failed to cache metadata {}: {}", cacheKey, e.getMessage());
        }
    }

    public Optional<Object> getMetadata(String cacheKey) {
        var key = METADATA_PREFIX + cacheKey;
        var cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            cacheHitCounter.increment();
            try {
                return Optional.of(objectMapper.readValue(cached, Object.class));
            } catch (JsonProcessingException e) {
                log.warn("Failed to deserialize metadata {}: {}", cacheKey, e.getMessage());
                cacheMissCounter.increment();
                return Optional.empty();
            }
        }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void evictMetadata(String cacheKey) {
        redisTemplate.delete(METADATA_PREFIX + cacheKey);
    }

    public void invalidateAll() {
        var keysToDelete = Stream.of(
                        CONFIGURATION_PREFIX + "*",
                        FEATURE_FLAG_PREFIX + "*",
                        ENVIRONMENT_PREFIX + "*",
                        SNAPSHOT_PREFIX + "*",
                        METADATA_PREFIX + "*"
                )
                .flatMap(pattern -> redisTemplate.keys(pattern).stream())
                .collect(Collectors.toSet());

        if (!keysToDelete.isEmpty()) {
            redisTemplate.delete(keysToDelete);
            log.info("Invalidated {} admin cache keys", keysToDelete.size());
        }
    }
}
