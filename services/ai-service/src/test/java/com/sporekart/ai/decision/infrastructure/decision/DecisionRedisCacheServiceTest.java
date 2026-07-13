package com.sporekart.ai.decision.infrastructure.decision;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.sporekart.ai.decision.infrastructure.redis.DecisionRedisCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import java.util.concurrent.TimeUnit;

@ExtendWith(MockitoExtension.class)
class DecisionRedisCacheServiceTest {

    @Mock private RedisTemplate<String, Object> redisTemplate;
    @Mock private ValueOperations<String, Object> valueOps;

    private DecisionRedisCacheService cacheService;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        cacheService = new DecisionRedisCacheService(redisTemplate);
    }

    @Test
    void cacheResultUsesCorrectPrefix() {
        cacheService.cacheResult("key1", "value1");
        verify(valueOps).set("decision:result:key1", "value1", 300L, TimeUnit.SECONDS);
    }

    @Test
    void getResultReturnsExpectedValue() {
        when(valueOps.get("decision:result:key1")).thenReturn("cachedValue");
        Object result = cacheService.getResult("key1");
        assertEquals("cachedValue", result);
    }

    @Test
    void evictResultDeletesCorrectKey() {
        cacheService.evictResult("key1");
        verify(redisTemplate).delete("decision:result:key1");
    }

    @Test
    void cacheMetadataUsesCorrectPrefix() {
        cacheService.cacheMetadata("meta1", "metaVal");
        verify(valueOps).set("decision:metadata:meta1", "metaVal", 300L, TimeUnit.SECONDS);
    }

    @Test
    void getMetadataReturnsExpectedValue() {
        when(valueOps.get("decision:metadata:meta1")).thenReturn("metaCached");
        Object result = cacheService.getMetadata("meta1");
        assertEquals("metaCached", result);
    }

    @Test
    void cacheRegistryUsesCorrectPrefix() {
        cacheService.cacheRegistry("reg1", "regVal");
        verify(valueOps).set("decision:registry:reg1", "regVal", 300L, TimeUnit.SECONDS);
    }

    @Test
    void getRegistryReturnsExpectedValue() {
        when(valueOps.get("decision:registry:reg1")).thenReturn("regCached");
        Object result = cacheService.getRegistry("reg1");
        assertEquals("regCached", result);
    }

    @Test
    void cacheStatisticsUsesCorrectPrefix() {
        cacheService.cacheStatistics("stat1", "statVal");
        verify(valueOps).set("decision:stats:stat1", "statVal", 120L, TimeUnit.SECONDS);
    }

    @Test
    void getStatisticsReturnsExpectedValue() {
        when(valueOps.get("decision:stats:stat1")).thenReturn("statCached");
        Object result = cacheService.getStatistics("stat1");
        assertEquals("statCached", result);
    }

    @Test
    void cacheExplanationUsesCorrectPrefix() {
        cacheService.cacheExplanation("expl1", "explVal");
        verify(valueOps).set("decision:explanation:expl1", "explVal", 300L, TimeUnit.SECONDS);
    }

    @Test
    void getExplanationReturnsExpectedValue() {
        when(valueOps.get("decision:explanation:expl1")).thenReturn("explCached");
        Object result = cacheService.getExplanation("expl1");
        assertEquals("explCached", result);
    }
}
