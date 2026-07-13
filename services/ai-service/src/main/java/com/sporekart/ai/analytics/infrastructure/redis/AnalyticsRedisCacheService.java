package com.sporekart.ai.analytics.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Profile("!test")
@Slf4j
@Service
public class AnalyticsRedisCacheService {

    private static final String DASHBOARD_PREFIX = "analytics:dashboard:";
    private static final String METRICS_PREFIX = "analytics:metrics:";
    private static final String KPIS_PREFIX = "analytics:kpis:";
    private static final String REPORTS_PREFIX = "analytics:reports:";
    private static final String STATISTICS_PREFIX = "analytics:statistics:";

    private static final long DASHBOARD_TTL = 300;
    private static final long METRICS_TTL = 120;
    private static final long KPIS_TTL = 300;
    private static final long REPORTS_TTL = 300;
    private static final long STATISTICS_TTL = 120;

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;
    private final Counter cacheWriteCounter;

    public AnalyticsRedisCacheService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper,
                                      MeterRegistry meterRegistry) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cacheHitCounter = Counter.builder("analytics.cache.hits")
                .description("Analytics cache hit count").register(meterRegistry);
        this.cacheMissCounter = Counter.builder("analytics.cache.misses")
                .description("Analytics cache miss count").register(meterRegistry);
        this.cacheWriteCounter = Counter.builder("analytics.cache.writes")
                .description("Analytics cache write count").register(meterRegistry);
    }

    public void cacheDashboard(String key, String json) {
        try {
            redisTemplate.opsForValue().set(DASHBOARD_PREFIX + key, json, DASHBOARD_TTL, TimeUnit.SECONDS);
            cacheWriteCounter.increment();
        } catch (Exception e) {
            log.warn("Failed to cache dashboard {}: {}", key, e.getMessage());
        }
    }

    public Optional<String> getDashboard(String key) {
        var cached = redisTemplate.opsForValue().get(DASHBOARD_PREFIX + key);
        if (cached != null) { cacheHitCounter.increment(); return Optional.of(cached); }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void evictDashboard(String key) {
        redisTemplate.delete(DASHBOARD_PREFIX + key);
    }

    public void cacheMetrics(String key, String json) {
        try {
            redisTemplate.opsForValue().set(METRICS_PREFIX + key, json, METRICS_TTL, TimeUnit.SECONDS);
            cacheWriteCounter.increment();
        } catch (Exception e) {
            log.warn("Failed to cache metrics {}: {}", key, e.getMessage());
        }
    }

    public Optional<String> getMetrics(String key) {
        var cached = redisTemplate.opsForValue().get(METRICS_PREFIX + key);
        if (cached != null) { cacheHitCounter.increment(); return Optional.of(cached); }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void evictMetrics(String key) {
        redisTemplate.delete(METRICS_PREFIX + key);
    }

    public void cacheKPIs(String key, String json) {
        try {
            redisTemplate.opsForValue().set(KPIS_PREFIX + key, json, KPIS_TTL, TimeUnit.SECONDS);
            cacheWriteCounter.increment();
        } catch (Exception e) {
            log.warn("Failed to cache KPIs {}: {}", key, e.getMessage());
        }
    }

    public Optional<String> getKPIs(String key) {
        var cached = redisTemplate.opsForValue().get(KPIS_PREFIX + key);
        if (cached != null) { cacheHitCounter.increment(); return Optional.of(cached); }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void evictKPIs(String key) {
        redisTemplate.delete(KPIS_PREFIX + key);
    }

    public void cacheReports(String key, String json) {
        try {
            redisTemplate.opsForValue().set(REPORTS_PREFIX + key, json, REPORTS_TTL, TimeUnit.SECONDS);
            cacheWriteCounter.increment();
        } catch (Exception e) {
            log.warn("Failed to cache reports {}: {}", key, e.getMessage());
        }
    }

    public Optional<String> getReports(String key) {
        var cached = redisTemplate.opsForValue().get(REPORTS_PREFIX + key);
        if (cached != null) { cacheHitCounter.increment(); return Optional.of(cached); }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void evictReports(String key) {
        redisTemplate.delete(REPORTS_PREFIX + key);
    }

    public void cacheStatistics(String key, String json) {
        try {
            redisTemplate.opsForValue().set(STATISTICS_PREFIX + key, json, STATISTICS_TTL, TimeUnit.SECONDS);
            cacheWriteCounter.increment();
        } catch (Exception e) {
            log.warn("Failed to cache statistics {}: {}", key, e.getMessage());
        }
    }

    public Optional<String> getStatistics(String key) {
        var cached = redisTemplate.opsForValue().get(STATISTICS_PREFIX + key);
        if (cached != null) { cacheHitCounter.increment(); return Optional.of(cached); }
        cacheMissCounter.increment();
        return Optional.empty();
    }

    public void evictStatistics(String key) {
        redisTemplate.delete(STATISTICS_PREFIX + key);
    }

    public void invalidateAll() {
        var patterns = Set.of(
                DASHBOARD_PREFIX + "*",
                METRICS_PREFIX + "*",
                KPIS_PREFIX + "*",
                REPORTS_PREFIX + "*",
                STATISTICS_PREFIX + "*"
        );
        long total = 0;
        for (var pattern : patterns) {
            var keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                total += keys.size();
            }
        }
        log.info("Invalidated {} analytics cache keys", total);
    }
}
