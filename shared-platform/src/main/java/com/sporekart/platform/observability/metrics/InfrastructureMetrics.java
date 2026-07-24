package com.sporekart.platform.observability.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Gauge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InfrastructureMetrics {

    private static final Logger log = LoggerFactory.getLogger(InfrastructureMetrics.class);
    private final MeterRegistry registry;

    private final Counter containerRestarts;
    private final Counter serviceDownEvents;
    private final Counter databaseFailures;
    private final Counter cacheFailures;
    private final Counter networkErrors;
    private final AtomicLong activeConnections;
    private final AtomicLong openFiles;
    private final AtomicInteger threadCount;

    public InfrastructureMetrics(MeterRegistry registry) {
        this.registry = registry;

        this.containerRestarts = Counter.builder("sporekart.infrastructure.container.restarts")
            .description("Container restart count").register(registry);
        this.serviceDownEvents = Counter.builder("sporekart.infrastructure.service.down")
            .description("Service down events").register(registry);
        this.databaseFailures = Counter.builder("sporekart.infrastructure.database.failures")
            .description("Database connection failures").register(registry);
        this.cacheFailures = Counter.builder("sporekart.infrastructure.cache.failures")
            .description("Cache connection failures").register(registry);
        this.networkErrors = Counter.builder("sporekart.infrastructure.network.errors")
            .description("Network error count").register(registry);

        this.activeConnections = registry.gauge("sporekart.infrastructure.connections.active",
            new AtomicLong(0));
        this.openFiles = registry.gauge("sporekart.infrastructure.files.open",
            new AtomicLong(0));
        this.threadCount = registry.gauge("sporekart.infrastructure.threads.total",
            new AtomicInteger(0));

        log.info("Infrastructure metrics initialized: 5 counters, 3 gauges");
    }

    public void recordContainerRestart(String service) {
        Counter.builder("sporekart.infrastructure.container.restarts")
            .tag("service", service).register(registry).increment();
        containerRestarts.increment();
    }

    public void recordServiceDown(String service) {
        Counter.builder("sporekart.infrastructure.service.down")
            .tag("service", service).register(registry).increment();
        serviceDownEvents.increment();
    }

    public void recordDatabaseFailure(String type) {
        Counter.builder("sporekart.infrastructure.database.failures")
            .tag("type", type).register(registry).increment();
        databaseFailures.increment();
    }

    public void recordCacheFailure(String cache) {
        Counter.builder("sporekart.infrastructure.cache.failures")
            .tag("cache", cache).register(registry).increment();
        cacheFailures.increment();
    }

    public void recordNetworkError(String type) {
        Counter.builder("sporekart.infrastructure.network.errors")
            .tag("type", type).register(registry).increment();
        networkErrors.increment();
    }

    public void setActiveConnections(long count) { activeConnections.set(count); }
    public void setOpenFiles(long count) { openFiles.set(count); }
    public void setThreadCount(int count) { threadCount.set(count); }
}
