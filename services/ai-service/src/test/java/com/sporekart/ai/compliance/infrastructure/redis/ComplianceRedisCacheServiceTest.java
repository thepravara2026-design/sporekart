package com.sporekart.ai.compliance.infrastructure.redis;

import com.sporekart.ai.compliance.domain.ComplianceRule;
import com.sporekart.ai.compliance.domain.RiskLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComplianceRedisCacheServiceTest {

    @Mock private RedisTemplate<String, Object> redisTemplate;
    @Mock private ValueOperations<String, Object> valueOps;

    private ComplianceRedisCacheService cacheService;

    @BeforeEach
    void setUp() {
        cacheService = new ComplianceRedisCacheService(redisTemplate);
    }

    @Test
    void testCacheRules() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        String frameworkId = UUID.randomUUID().toString();
        List<ComplianceRule> rules = List.of(
            new ComplianceRule(UUID.randomUUID(), UUID.randomUUID(), "R1", "Rule", "desc", "cat",
                RiskLevel.LOW, "expr", true, Instant.now(), Instant.now().plusSeconds(86400))
        );

        cacheService.cacheRules(frameworkId, rules);

        verify(valueOps).set(eq("compliance:rules:" + frameworkId), eq(rules), anyLong(), any());
    }

    @Test
    void testGetRulesFound() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        String frameworkId = UUID.randomUUID().toString();
        List<ComplianceRule> rules = List.of(
            new ComplianceRule(UUID.randomUUID(), UUID.randomUUID(), "R1", "Rule", "desc", "cat",
                RiskLevel.LOW, "expr", true, Instant.now(), Instant.now().plusSeconds(86400))
        );
        when(valueOps.get("compliance:rules:" + frameworkId)).thenReturn((Object) rules);

        Optional<List<ComplianceRule>> result = cacheService.getRules(frameworkId);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
    }

    @Test
    void testGetRulesNotFound() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        String frameworkId = UUID.randomUUID().toString();
        when(valueOps.get("compliance:rules:" + frameworkId)).thenReturn(null);

        Optional<List<ComplianceRule>> result = cacheService.getRules(frameworkId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetRulesReturnsEmptyForWrongType() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        String frameworkId = UUID.randomUUID().toString();
        when(valueOps.get("compliance:rules:" + frameworkId)).thenReturn("not-a-list");

        Optional<List<ComplianceRule>> result = cacheService.getRules(frameworkId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testEvictRules() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        String frameworkId = UUID.randomUUID().toString();

        cacheService.evictRules(frameworkId);

        verify(redisTemplate).delete("compliance:rules:" + frameworkId);
    }

    @Test
    void testCacheFramework() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        cacheService.cacheFramework("fw1", null);

        verify(valueOps).set(eq("compliance:frameworks:fw1"), isNull(), anyLong(), any());
    }

    @Test
    void testCacheValidation() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        cacheService.cacheValidation("assess1", Map.of("key", "value"));

        verify(valueOps).set(eq("compliance:validation:assess1"), any(), anyLong(), any());
    }

    @Test
    void testCacheReport() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        cacheService.cacheReport("report1", null);

        verify(valueOps).set(eq("compliance:reports:report1"), isNull(), anyLong(), any());
    }

    @Test
    void testInvalidateAll() {
        Set<String> keys = Set.of("compliance:rules:fw1", "compliance:frameworks:fw1");
        when(redisTemplate.keys("compliance:*")).thenReturn(keys);

        cacheService.invalidateAll();

        verify(redisTemplate).delete(keys);
    }

    @Test
    void testInvalidateAllWithNoKeys() {
        when(redisTemplate.keys("compliance:*")).thenReturn(null);

        cacheService.invalidateAll();

        verify(redisTemplate, never()).delete(any());
    }
}
