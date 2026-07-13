package com.sporekart.ai.semantic.infrastructure.monitoring;

import com.sporekart.ai.semantic.application.SemanticIndexService;
import com.sporekart.ai.semantic.application.SemanticEmbeddingService;
import com.sporekart.ai.semantic.infrastructure.SemanticRedisCacheService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
public class SemanticMonitoringService {

    private static final Logger log = LoggerFactory.getLogger(SemanticMonitoringService.class);

    private final MeterRegistry meterRegistry;
    private final SemanticIndexService indexService;
    private final SemanticEmbeddingService embeddingService;
    private final SemanticRedisCacheService cacheService;
    private final Map<String, Timer> timers = new ConcurrentHashMap<>();

    public SemanticMonitoringService(MeterRegistry meterRegistry,
                                     SemanticIndexService indexService,
                                     SemanticEmbeddingService embeddingService,
                                     SemanticRedisCacheService cacheService) {
        this.meterRegistry = meterRegistry;
        this.indexService = indexService;
        this.embeddingService = embeddingService;
        this.cacheService = cacheService;
    }

    public <T> T recordEmbedLatency(Supplier<T> operation) {
        Timer timer = timers.computeIfAbsent("semantic.embed.latency",
                k -> meterRegistry.timer("semantic.embed.latency"));
        return timer.record(() -> {
            long start = System.nanoTime();
            try {
                return operation.get();
            } finally {
                long elapsed = System.nanoTime() - start;
                log.debug("Embed operation took {} ms", TimeUnit.NANOSECONDS.toMillis(elapsed));
            }
        });
    }

    public <T> T recordSearchLatency(Supplier<T> operation) {
        Timer timer = timers.computeIfAbsent("semantic.search.latency",
                k -> meterRegistry.timer("semantic.search.latency"));
        return timer.record(() -> {
            long start = System.nanoTime();
            try {
                return operation.get();
            } finally {
                long elapsed = System.nanoTime() - start;
                log.debug("Search operation took {} ms", TimeUnit.NANOSECONDS.toMillis(elapsed));
            }
        });
    }

    public void recordCacheHit(String cacheName) {
        meterRegistry.counter("semantic.cache.hit", "cache", cacheName).increment();
    }

    public void recordCacheMiss(String cacheName) {
        meterRegistry.counter("semantic.cache.miss", "cache", cacheName).increment();
    }

    public void recordEmbeddingCreated(String provider) {
        meterRegistry.counter("semantic.embedding.created", "provider", provider).increment();
    }

    public void recordSearchExecuted(String searchType) {
        meterRegistry.counter("semantic.search.executed", "type", searchType).increment();
    }

    public double getCacheHitRatio(String cacheName) {
        double hits = meterRegistry.counter("semantic.cache.hit", "cache", cacheName).count();
        double misses = meterRegistry.counter("semantic.cache.miss", "cache", cacheName).count();
        double total = hits + misses;
        return total == 0 ? 0.0 : hits / total;
    }

    public HealthStatus checkHealth() {
        try {
            long embedCount = embeddingService.listEmbeddings().size();
            long indexCount = indexService.listIndexes().size();
            boolean cacheAvailable = cacheService != null;

            log.info("Health check: embeddings={}, indexes={}, cache={}",
                    embedCount, indexCount, cacheAvailable);

            return new HealthStatus("UP", embedCount, indexCount, cacheAvailable);
        } catch (Exception e) {
            log.warn("Health check failed: {}", e.getMessage());
            return new HealthStatus("DOWN", 0, 0, false);
        }
    }

    public record HealthStatus(String status, long embeddingCount, long indexCount, boolean cacheAvailable) {}
}
