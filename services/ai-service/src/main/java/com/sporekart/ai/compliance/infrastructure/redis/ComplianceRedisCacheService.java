package com.sporekart.ai.compliance.infrastructure.redis;

import com.sporekart.ai.compliance.domain.ComplianceFramework;
import com.sporekart.ai.compliance.domain.ComplianceReport;
import com.sporekart.ai.compliance.domain.ComplianceRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplianceRedisCacheService {

    private static final String RULES_NAMESPACE = "compliance:rules:";
    private static final long RULES_TTL = 300;

    private static final String FRAMEWORKS_NAMESPACE = "compliance:frameworks:";
    private static final long FRAMEWORKS_TTL = 300;

    private static final String VALIDATION_NAMESPACE = "compliance:validation:";
    private static final long VALIDATION_TTL = 180;

    private static final String REPORTS_NAMESPACE = "compliance:reports:";
    private static final long REPORTS_TTL = 300;

    private static final String STATISTICS_NAMESPACE = "compliance:statistics:";
    private static final long STATISTICS_TTL = 120;

    private final RedisTemplate<String, Object> redisTemplate;

    public void cacheRules(String frameworkId, List<ComplianceRule> rules) {
        String key = RULES_NAMESPACE + frameworkId;
        redisTemplate.opsForValue().set(key, rules, RULES_TTL, TimeUnit.SECONDS);
        log.debug("Cached {} rules for framework {}", rules.size(), frameworkId);
    }

    @SuppressWarnings("unchecked")
    public Optional<List<ComplianceRule>> getRules(String frameworkId) {
        String key = RULES_NAMESPACE + frameworkId;
        Object value = redisTemplate.opsForValue().get(key);
        if (value instanceof List) {
            return Optional.of((List<ComplianceRule>) value);
        }
        return Optional.empty();
    }

    public void evictRules(String frameworkId) {
        String key = RULES_NAMESPACE + frameworkId;
        redisTemplate.delete(key);
        log.debug("Evicted rules cache for framework {}", frameworkId);
    }

    public void cacheFramework(String frameworkId, ComplianceFramework framework) {
        String key = FRAMEWORKS_NAMESPACE + frameworkId;
        redisTemplate.opsForValue().set(key, framework, FRAMEWORKS_TTL, TimeUnit.SECONDS);
        log.debug("Cached framework {}", frameworkId);
    }

    public Optional<ComplianceFramework> getFramework(String frameworkId) {
        String key = FRAMEWORKS_NAMESPACE + frameworkId;
        Object value = redisTemplate.opsForValue().get(key);
        if (value instanceof ComplianceFramework) {
            return Optional.of((ComplianceFramework) value);
        }
        return Optional.empty();
    }

    public void cacheValidation(String assessmentId, Map<String, Object> validationResult) {
        String key = VALIDATION_NAMESPACE + assessmentId;
        redisTemplate.opsForValue().set(key, validationResult, VALIDATION_TTL, TimeUnit.SECONDS);
        log.debug("Cached validation result for assessment {}", assessmentId);
    }

    @SuppressWarnings("unchecked")
    public Optional<Map<String, Object>> getValidation(String assessmentId) {
        String key = VALIDATION_NAMESPACE + assessmentId;
        Object value = redisTemplate.opsForValue().get(key);
        if (value instanceof Map) {
            return Optional.of((Map<String, Object>) value);
        }
        return Optional.empty();
    }

    public void cacheReport(String reportId, ComplianceReport report) {
        String key = REPORTS_NAMESPACE + reportId;
        redisTemplate.opsForValue().set(key, report, REPORTS_TTL, TimeUnit.SECONDS);
        log.debug("Cached report {}", reportId);
    }

    public Optional<ComplianceReport> getReport(String reportId) {
        String key = REPORTS_NAMESPACE + reportId;
        Object value = redisTemplate.opsForValue().get(key);
        if (value instanceof ComplianceReport) {
            return Optional.of((ComplianceReport) value);
        }
        return Optional.empty();
    }

    public void evictReport(String reportId) {
        String key = REPORTS_NAMESPACE + reportId;
        redisTemplate.delete(key);
        log.debug("Evicted report cache for {}", reportId);
    }

    public void cacheStatistics(String metricKey, Object statistics) {
        String key = STATISTICS_NAMESPACE + metricKey;
        redisTemplate.opsForValue().set(key, statistics, STATISTICS_TTL, TimeUnit.SECONDS);
        log.debug("Cached statistics for metric {}", metricKey);
    }

    public Optional<Object> getStatistics(String metricKey) {
        String key = STATISTICS_NAMESPACE + metricKey;
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    public void invalidateAll() {
        Set<String> keys = redisTemplate.keys("compliance:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("Invalidated all compliance caches ({} keys)", keys.size());
        }
    }
}
