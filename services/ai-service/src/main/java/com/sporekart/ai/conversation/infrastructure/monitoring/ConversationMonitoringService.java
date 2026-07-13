package com.sporekart.ai.conversation.infrastructure.monitoring;

import com.sporekart.ai.conversation.api.MessageService;
import com.sporekart.ai.conversation.api.SessionManager;
import com.sporekart.ai.conversation.infrastructure.redis.ConversationRedisCacheService;
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
public class ConversationMonitoringService {

    private static final Logger log = LoggerFactory.getLogger(ConversationMonitoringService.class);

    private final MeterRegistry meterRegistry;
    private final SessionManager sessionManager;
    private final MessageService messageService;
    private final ConversationRedisCacheService cacheService;
    private final Map<String, Timer> timers = new ConcurrentHashMap<>();

    public ConversationMonitoringService(MeterRegistry meterRegistry,
                                         SessionManager sessionManager,
                                         MessageService messageService,
                                         ConversationRedisCacheService cacheService) {
        this.meterRegistry = meterRegistry;
        this.sessionManager = sessionManager;
        this.messageService = messageService;
        this.cacheService = cacheService;
    }

    public <T> T recordSessionLatency(Supplier<T> operation) {
        var timer = timers.computeIfAbsent("conversation.session.latency",
                k -> meterRegistry.timer("conversation.session.latency"));
        return timer.record(() -> {
            long start = System.nanoTime();
            try {
                return operation.get();
            } finally {
                long elapsed = System.nanoTime() - start;
                log.debug("Session operation took {} ms", TimeUnit.NANOSECONDS.toMillis(elapsed));
            }
        });
    }

    public <T> T recordMessageLatency(Supplier<T> operation) {
        var timer = timers.computeIfAbsent("conversation.message.latency",
                k -> meterRegistry.timer("conversation.message.latency"));
        return timer.record(() -> {
            long start = System.nanoTime();
            try {
                return operation.get();
            } finally {
                long elapsed = System.nanoTime() - start;
                log.debug("Message operation took {} ms", TimeUnit.NANOSECONDS.toMillis(elapsed));
            }
        });
    }

    public void recordMessageSent(String role) {
        meterRegistry.counter("conversation.message.sent", "role", role).increment();
    }

    public void recordSessionCreated() {
        meterRegistry.counter("conversation.session.created").increment();
    }

    public void recordSessionClosed() {
        meterRegistry.counter("conversation.session.closed").increment();
    }

    public void recordCacheHit(String cacheName) {
        meterRegistry.counter("conversation.cache.hit", "cache", cacheName).increment();
    }

    public void recordCacheMiss(String cacheName) {
        meterRegistry.counter("conversation.cache.miss", "cache", cacheName).increment();
    }

    public double getCacheHitRatio(String cacheName) {
        double hits = meterRegistry.counter("conversation.cache.hit", "cache", cacheName).count();
        double misses = meterRegistry.counter("conversation.cache.miss", "cache", cacheName).count();
        double total = hits + misses;
        return total == 0 ? 0.0 : hits / total;
    }

    public HealthStatus checkHealth() {
        try {
            int sessionCount = sessionManager.getUserSessions("health-check").size();
            boolean cacheAvailable = cacheService != null;
            log.info("Health check: sessions={}, cache={}", sessionCount, cacheAvailable);
            return new HealthStatus("UP", sessionCount, cacheAvailable);
        } catch (Exception e) {
            log.warn("Health check failed: {}", e.getMessage());
            return new HealthStatus("DOWN", 0, false);
        }
    }

    public record HealthStatus(String status, int sessionCount, boolean cacheAvailable) {}
}
