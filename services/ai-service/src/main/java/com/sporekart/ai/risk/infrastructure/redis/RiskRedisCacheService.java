package com.sporekart.ai.risk.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.risk.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RiskRedisCacheService {

    private static final String SCORES_PREFIX = "risk:scores:";
    private static final String TRUST_PREFIX = "risk:trust:";
    private static final String CONFIDENCE_PREFIX = "risk:confidence:";
    private static final String THRESHOLDS_PREFIX = "risk:thresholds:";
    private static final String METADATA_PREFIX = "risk:metadata:";

    private static final long SCORES_TTL = 300;
    private static final long TRUST_TTL = 300;
    private static final long CONFIDENCE_TTL = 300;
    private static final long THRESHOLDS_TTL = 600;
    private static final long METADATA_TTL = 300;

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public void cacheScore(String key, RiskScore score) {
        set(SCORES_PREFIX + key, score, SCORES_TTL);
    }

    public RiskScore getScore(String key) {
        return get(SCORES_PREFIX + key, RiskScore.class);
    }

    public void evictScore(String key) {
        redisTemplate.delete(SCORES_PREFIX + key);
    }

    public void cacheTrust(String key, TrustAssessment trust) {
        set(TRUST_PREFIX + key, trust, TRUST_TTL);
    }

    public TrustAssessment getTrust(String key) {
        return get(TRUST_PREFIX + key, TrustAssessment.class);
    }

    public void evictTrust(String key) {
        redisTemplate.delete(TRUST_PREFIX + key);
    }

    public void cacheConfidence(String key, ConfidenceScore confidence) {
        set(CONFIDENCE_PREFIX + key, confidence, CONFIDENCE_TTL);
    }

    public ConfidenceScore getConfidence(String key) {
        return get(CONFIDENCE_PREFIX + key, ConfidenceScore.class);
    }

    public void evictConfidence(String key) {
        redisTemplate.delete(CONFIDENCE_PREFIX + key);
    }

    public void cacheThreshold(String key, RiskThreshold threshold) {
        set(THRESHOLDS_PREFIX + key, threshold, THRESHOLDS_TTL);
    }

    public RiskThreshold getThreshold(String key) {
        return get(THRESHOLDS_PREFIX + key, RiskThreshold.class);
    }

    public void evictThreshold(String key) {
        redisTemplate.delete(THRESHOLDS_PREFIX + key);
    }

    public void cacheMetadata(String key, Map<String, Object> metadata) {
        set(METADATA_PREFIX + key, metadata, METADATA_TTL);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getMetadata(String key) {
        return get(METADATA_PREFIX + key, Map.class);
    }

    public void evictMetadata(String key) {
        redisTemplate.delete(METADATA_PREFIX + key);
    }

    public void invalidateAll() {
        redisTemplate.delete(redisTemplate.keys("risk:*"));
        log.info("All risk cache entries invalidated");
    }

    private <T> void set(String key, T value, long ttlSeconds) {
        try {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(value), ttlSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Failed to cache value for key: {}", key, e);
        }
    }

    private <T> T get(String key, Class<T> type) {
        try {
            var value = redisTemplate.opsForValue().get(key);
            if (value == null) return null;
            return objectMapper.readValue(value, type);
        } catch (Exception e) {
            log.error("Failed to retrieve cached value for key: {}", key, e);
            return null;
        }
    }
}
