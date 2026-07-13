package com.sporekart.ai.integration;

import com.sporekart.ai.governance.infrastructure.redis.GovernanceRedisCacheService;
import com.sporekart.ai.governance.config.GovernanceConfig;
import com.sporekart.ai.policy.infrastructure.redis.PolicyRedisCacheService;
import com.sporekart.ai.decision.infrastructure.redis.DecisionRedisCacheService;
import com.sporekart.ai.approval.infrastructure.redis.ApprovalRedisCacheService;
import com.sporekart.ai.approval.domain.ApprovalRequest;
import com.sporekart.ai.approval.domain.ApprovalStatus;
import com.sporekart.ai.compliance.infrastructure.redis.ComplianceRedisCacheService;
import com.sporekart.ai.compliance.domain.ComplianceRule;
import com.sporekart.ai.risk.infrastructure.redis.RiskRedisCacheService;
import com.sporekart.ai.risk.domain.RiskScore;
import com.sporekart.ai.risk.domain.RiskLevel;
import com.sporekart.ai.risk.domain.RiskCategory;
import com.sporekart.ai.analytics.infrastructure.redis.AnalyticsRedisCacheService;
import com.sporekart.ai.admin.infrastructure.redis.AdminRedisCacheService;
import com.sporekart.ai.admin.domain.*;
import com.sporekart.ai.automation.infrastructure.redis.AutomationRedisCacheService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisCacheIntegrationTest {

    @Mock private RedisTemplate<String, Object> redisTemplate;
    @Mock private StringRedisTemplate stringRedisTemplate;
    @Mock private ValueOperations<String, Object> valueOperations;
    @Mock private ValueOperations<String, String> stringValueOperations;
    @Mock private GovernanceConfig governanceConfig;
    @Mock private GovernanceConfig.CacheConfig cacheConfig;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(stringRedisTemplate.opsForValue()).thenReturn(stringValueOperations);
        when(governanceConfig.getCache()).thenReturn(cacheConfig);
        when(cacheConfig.getConfigTtlSeconds()).thenReturn(300L);
        when(cacheConfig.getRegistryTtlSeconds()).thenReturn(300L);
        when(cacheConfig.getHealthTtlSeconds()).thenReturn(60L);
        when(cacheConfig.getMetricsTtlSeconds()).thenReturn(120L);
        when(cacheConfig.getValidationTtlSeconds()).thenReturn(180L);
    }

    @Test
    void testGovernanceConfigCaching() {
        GovernanceRedisCacheService cacheService = new GovernanceRedisCacheService(redisTemplate, governanceConfig);

        cacheService.cacheConfiguration("pricing_rules", Map.of("maxDiscount", 0.3));
        verify(valueOperations).set(
            eq("gov:config:pricing_rules"),
            eq(Map.of("maxDiscount", 0.3)),
            eq(300L),
            eq(TimeUnit.SECONDS)
        );

        when(valueOperations.get("gov:config:pricing_rules"))
            .thenReturn(Map.of("maxDiscount", 0.3));
        Object cached = cacheService.getConfiguration("pricing_rules");
        assertNotNull(cached);
        assertInstanceOf(Map.class, cached);

        cacheService.evictConfiguration("pricing_rules");
        verify(redisTemplate).delete("gov:config:pricing_rules");
    }

    @Test
    void testPolicyRulesCaching() {
        PolicyRedisCacheService cacheService = new PolicyRedisCacheService(redisTemplate);

        cacheService.cacheRegistry("policy-1", "active");
        verify(valueOperations).set(
            eq("policy:registry:policy-1"), eq("active"), eq(300L), eq(TimeUnit.SECONDS)
        );

        when(valueOperations.get("policy:compiled:price-rule")).thenReturn("true");
        cacheService.cacheCompiled("price-rule", "true");
        verify(valueOperations).set(
            eq("policy:compiled:price-rule"), eq("true"), eq(600L), eq(TimeUnit.SECONDS)
        );

        cacheService.cacheEvaluation("eval-1", "passed");
        verify(valueOperations).set(
            eq("policy:evaluation:eval-1"), eq("passed"), eq(180L), eq(TimeUnit.SECONDS)
        );

        cacheService.evictCompiled("price-rule");
        verify(redisTemplate).delete("policy:compiled:price-rule");
    }

    @Test
    void testDecisionResultsCaching() {
        DecisionRedisCacheService cacheService = new DecisionRedisCacheService(redisTemplate);

        cacheService.cacheResult("dec-1", "ALLOWED");
        verify(valueOperations).set(
            eq("decision:result:dec-1"), eq("ALLOWED"), eq(300L), eq(TimeUnit.SECONDS)
        );

        when(valueOperations.get("decision:result:dec-1")).thenReturn("ALLOWED");
        Object result = cacheService.getResult("dec-1");
        assertEquals("ALLOWED", result);

        cacheService.cacheStatistics("stats-1", Map.of("total", 100));
        verify(valueOperations).set(
            eq("decision:stats:stats-1"), eq(Map.of("total", 100)), eq(120L), eq(TimeUnit.SECONDS)
        );

        cacheService.evictResult("dec-1");
        verify(redisTemplate).delete("decision:result:dec-1");
    }

    @Test
    void testApprovalAssignmentsCaching() {
        ApprovalRedisCacheService cacheService = new ApprovalRedisCacheService(
            stringRedisTemplate, objectMapper
        );

        UUID approvalId = UUID.randomUUID();
        ApprovalRequest request = new ApprovalRequest(
            approvalId, "catalog", "publish", Map.of(), Map.of(),
            "user1", List.of("admin"), "test", "low",
            UUID.randomUUID(), Map.of(), OffsetDateTime.now().plusDays(1),
            ApprovalStatus.PENDING, OffsetDateTime.now()
        );

        cacheService.cachePending("pending-1", request);
        verify(stringRedisTemplate.opsForValue()).set(
            eq("approval:pending:pending-1"), anyString(), eq(60L), eq(TimeUnit.SECONDS)
        );

        cacheService.evictPending("pending-1");
        verify(stringRedisTemplate).delete("approval:pending:pending-1");
    }

    @Test
    void testComplianceRulesCaching() {
        ComplianceRedisCacheService cacheService = new ComplianceRedisCacheService(redisTemplate);

        ComplianceRule rule = new ComplianceRule(
            UUID.randomUUID(), UUID.randomUUID(), "AML-001",
            "Anti-Money Laundering Check", "Verify transaction origin",
            "finance", RiskLevel.HIGH, "amount > 10000", true,
            Instant.now(), Instant.now()
        );
        cacheService.cacheRules("framework-1", List.of(rule));
        verify(valueOperations).set(
            eq("compliance:rules:framework-1"),
            eq(List.of(rule)),
            eq(300L),
            eq(TimeUnit.SECONDS)
        );

        when(valueOperations.get("compliance:rules:framework-1"))
            .thenReturn(List.of(rule));
        Optional<List<ComplianceRule>> cached = cacheService.getRules("framework-1");
        assertTrue(cached.isPresent());
        assertEquals(1, cached.get().size());

        cacheService.evictRules("framework-1");
        verify(redisTemplate).delete("compliance:rules:framework-1");
    }

    @Test
    void testRiskScoresCaching() {
        RiskRedisCacheService cacheService = new RiskRedisCacheService(
            stringRedisTemplate, objectMapper
        );

        RiskScore score = new RiskScore(
            UUID.randomUUID(), UUID.randomUUID(), 0.45, RiskLevel.MEDIUM,
            Map.of(RiskCategory.OPERATIONAL, 0.45), 3, Instant.now()
        );

        cacheService.cacheScore("assess-1", score);
        verify(stringRedisTemplate.opsForValue()).set(
            eq("risk:scores:assess-1"), anyString(), eq(300L), eq(TimeUnit.SECONDS)
        );

        cacheService.evictScore("assess-1");
        verify(stringRedisTemplate).delete("risk:scores:assess-1");
    }

    @Test
    void testAnalyticsDataCaching() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        AnalyticsRedisCacheService cacheService = new AnalyticsRedisCacheService(
            stringRedisTemplate, objectMapper, meterRegistry
        );

        cacheService.cacheDashboard("dash-1", "{\"name\":\"Governance\"}");
        verify(stringRedisTemplate.opsForValue()).set(
            eq("analytics:dashboard:dash-1"), eq("{\"name\":\"Governance\"}"),
            eq(300L), eq(TimeUnit.SECONDS)
        );

        when(stringRedisTemplate.opsForValue().get("analytics:dashboard:dash-1"))
            .thenReturn("{\"name\":\"Governance\"}");
        Optional<String> cached = cacheService.getDashboard("dash-1");
        assertTrue(cached.isPresent());
        assertEquals("{\"name\":\"Governance\"}", cached.get());

        cacheService.evictDashboard("dash-1");
        verify(stringRedisTemplate).delete("analytics:dashboard:dash-1");
    }

    @Test
    void testAdminConfigurationCaching() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        AdminRedisCacheService cacheService = new AdminRedisCacheService(
            stringRedisTemplate, objectMapper, meterRegistry
        );

        UUID configId = UUID.randomUUID();
        AdminConfiguration config = new AdminConfiguration(
            configId, "pricing.maxDiscount", "0.3", "catalog",
            "production", "Max discount config",
            ConfigurationStatus.ACTIVE, 1, UUID.randomUUID(),
            Instant.now(), Instant.now()
        );

        cacheService.cacheConfiguration("pricing", config);
        verify(stringRedisTemplate.opsForValue()).set(
            eq("admin:configuration:pricing"), anyString(),
            eq(300L), eq(TimeUnit.SECONDS)
        );

        when(stringRedisTemplate.opsForValue().get("admin:configuration:pricing"))
            .thenReturn(objectMapper.writeValueAsString(config));
        Optional<AdminConfiguration> cached = cacheService.getConfiguration("pricing");
        assertTrue(cached.isPresent());
        assertEquals("pricing.maxDiscount", cached.get().key());

        cacheService.evictConfiguration("pricing");
        verify(stringRedisTemplate).delete("admin:configuration:pricing");
    }

    @Test
    void testAutomationConfigCaching() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        AutomationRedisCacheService cacheService = new AutomationRedisCacheService(
            stringRedisTemplate, objectMapper, meterRegistry
        );

        cacheService.cacheWorkflow("wf-1", "{\"steps\":[\"validate\",\"approve\"]}");
        verify(stringRedisTemplate.opsForValue()).set(
            eq("automation:workflow:wf-1"), eq("{\"steps\":[\"validate\",\"approve\"]}"),
            eq(300L), eq(TimeUnit.SECONDS)
        );

        when(stringRedisTemplate.opsForValue().get("automation:workflow:wf-1"))
            .thenReturn("{\"steps\":[\"validate\",\"approve\"]}");
        Optional<String> cached = cacheService.getWorkflow("wf-1");
        assertTrue(cached.isPresent());
        assertEquals("{\"steps\":[\"validate\",\"approve\"]}", cached.get());

        cacheService.evictWorkflow("wf-1");
        verify(stringRedisTemplate).delete("automation:workflow:wf-1");
    }

    @Test
    void testTTLAndEvictionStrategy() {
        GovernanceRedisCacheService govCache = new GovernanceRedisCacheService(redisTemplate, governanceConfig);

        govCache.cacheConfiguration("key1", "value1");
        verify(valueOperations).set(
            eq("gov:config:key1"), eq("value1"), eq(300L), eq(TimeUnit.SECONDS)
        );

        govCache.cacheValidation("val1", "result1");
        verify(valueOperations).set(
            eq("gov:validation:val1"), eq("result1"), eq(180L), eq(TimeUnit.SECONDS)
        );

        PolicyRedisCacheService policyCache = new PolicyRedisCacheService(redisTemplate);
        policyCache.cacheRegistry("reg1", "data");
        verify(valueOperations).set(
            eq("policy:registry:reg1"), eq("data"), eq(300L), eq(TimeUnit.SECONDS)
        );

        when(redisTemplate.keys("gov:*")).thenReturn(
            java.util.Set.of("gov:config:key1", "gov:validation:val1")
        );

        when(valueOperations.get("gov:config:key1")).thenReturn("value1");
        Object val = govCache.getConfiguration("key1");
        assertEquals("value1", val);

        govCache.invalidateAll();
        verify(redisTemplate).delete(anySet());
    }

    @Test
    void testCrossModuleCacheIndependence() {
        GovernanceRedisCacheService govCache = new GovernanceRedisCacheService(redisTemplate, governanceConfig);
        PolicyRedisCacheService policyCache = new PolicyRedisCacheService(redisTemplate);

        govCache.cacheConfiguration("shared-key", "gov-value");
        policyCache.cacheRegistry("shared-key", "policy-value");

        verify(valueOperations).set(
            eq("gov:config:shared-key"), eq("gov-value"), anyLong(), any()
        );
        verify(valueOperations).set(
            eq("policy:registry:shared-key"), eq("policy-value"), anyLong(), any()
        );

        when(valueOperations.get("gov:config:shared-key")).thenReturn("gov-value");
        when(valueOperations.get("policy:registry:shared-key")).thenReturn("policy-value");

        Object govVal = govCache.getConfiguration("shared-key");
        Object policyVal = policyCache.getRegistry("shared-key");

        assertEquals("gov-value", govVal);
        assertEquals("policy-value", policyVal);
        assertNotEquals(govVal, policyVal);
    }
}
