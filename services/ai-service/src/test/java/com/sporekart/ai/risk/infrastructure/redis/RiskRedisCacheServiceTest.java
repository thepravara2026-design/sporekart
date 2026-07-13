package com.sporekart.ai.risk.infrastructure.redis;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.risk.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

@ExtendWith(MockitoExtension.class)
class RiskRedisCacheServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;

    private ObjectMapper objectMapper;
    private RiskRedisCacheService cacheService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        cacheService = new RiskRedisCacheService(redisTemplate, objectMapper);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void cacheScoreShouldStoreInRedis() throws Exception {
        var key = "test-key";
        var score = new RiskScore(UUID.randomUUID(), UUID.randomUUID(), 50.0, RiskLevel.MEDIUM, Map.of(), 2, Instant.now());

        cacheService.cacheScore(key, score);

        verify(valueOperations).set(eq("risk:scores:" + key), anyString(), eq(300L), eq(TimeUnit.SECONDS));
    }

    @Test
    void getScoreShouldReturnFromRedis() throws Exception {
        var key = "test-key";
        var scoreJson = objectMapper.writeValueAsString(new RiskScore(UUID.randomUUID(), UUID.randomUUID(), 50.0, RiskLevel.MEDIUM, Map.of(), 2, Instant.now()));

        when(valueOperations.get("risk:scores:" + key)).thenReturn(scoreJson);

        var result = cacheService.getScore(key);

        assertNotNull(result);
        assertEquals(50.0, result.overallScore());
        assertEquals(RiskLevel.MEDIUM, result.riskLevel());
    }

    @Test
    void getScoreShouldReturnNullWhenNotCached() {
        when(valueOperations.get(anyString())).thenReturn(null);
        assertNull(cacheService.getScore("nonexistent"));
    }

    @Test
    void evictScoreShouldDeleteFromRedis() {
        var key = "test-key";

        cacheService.evictScore(key);

        verify(redisTemplate).delete("risk:scores:" + key);
    }

    @Test
    void cacheTrustShouldStoreInRedis() throws Exception {
        var key = "trust-key";
        var trust = new TrustAssessment(UUID.randomUUID(), UUID.randomUUID(), 85.0, Map.of(), Map.of(), Instant.now());

        cacheService.cacheTrust(key, trust);

        verify(valueOperations).set(eq("risk:trust:" + key), anyString(), eq(300L), eq(TimeUnit.SECONDS));
    }

    @Test
    void getTrustShouldReturnFromRedis() throws Exception {
        var key = "trust-key";
        var trustJson = objectMapper.writeValueAsString(new TrustAssessment(UUID.randomUUID(), UUID.randomUUID(), 85.0, Map.of(TrustFactor.PROVIDER_RELIABILITY, 85.0), Map.of(TrustFactor.PROVIDER_RELIABILITY, "ok"), Instant.now()));

        when(valueOperations.get("risk:trust:" + key)).thenReturn(trustJson);

        var result = cacheService.getTrust(key);

        assertNotNull(result);
        assertEquals(85.0, result.overallTrustScore());
    }

    @Test
    void evictTrustShouldDeleteFromRedis() {
        cacheService.evictTrust("trust-key");
        verify(redisTemplate).delete("risk:trust:trust-key");
    }

    @Test
    void cacheConfidenceShouldStoreInRedis() throws Exception {
        var key = "conf-key";
        var confidence = new ConfidenceScore(UUID.randomUUID(), UUID.randomUUID(), 70.0, Map.of(), "exp", Instant.now());

        cacheService.cacheConfidence(key, confidence);

        verify(valueOperations).set(eq("risk:confidence:" + key), anyString(), eq(300L), eq(TimeUnit.SECONDS));
    }

    @Test
    void getConfidenceShouldReturnFromRedis() throws Exception {
        var key = "conf-key";
        var confJson = objectMapper.writeValueAsString(new ConfidenceScore(UUID.randomUUID(), UUID.randomUUID(), 70.0, Map.of(), "explanation", Instant.now()));

        when(valueOperations.get("risk:confidence:" + key)).thenReturn(confJson);

        var result = cacheService.getConfidence(key);

        assertNotNull(result);
        assertEquals(70.0, result.overallConfidence());
    }

    @Test
    void evictConfidenceShouldDeleteFromRedis() {
        cacheService.evictConfidence("conf-key");
        verify(redisTemplate).delete("risk:confidence:conf-key");
    }

    @Test
    void cacheThresholdShouldStoreInRedis() throws Exception {
        var key = "LOW";
        var threshold = new RiskThreshold(UUID.randomUUID(), RiskLevel.LOW, 0, 20, "proceed");

        cacheService.cacheThreshold(key, threshold);

        verify(valueOperations).set(eq("risk:thresholds:" + key), anyString(), eq(600L), eq(TimeUnit.SECONDS));
    }

    @Test
    void cacheMetadataShouldStoreInRedis() throws Exception {
        var key = "meta-key";
        var metadata = Map.<String, Object>of("version", "1.0");

        cacheService.cacheMetadata(key, metadata);

        verify(valueOperations).set(eq("risk:metadata:" + key), anyString(), eq(300L), eq(TimeUnit.SECONDS));
    }

    @Test
    void invalidateAllShouldDeleteAllRiskKeys() {
        var keys = List.of("risk:scores:1", "risk:trust:2");
        when(redisTemplate.keys("risk:*")).thenReturn(keys);

        cacheService.invalidateAll();

        verify(redisTemplate).delete(keys);
    }
}
