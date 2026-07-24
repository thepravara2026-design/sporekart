package com.sporekart.platform.observability.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Gauge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class EventMetrics {

    private static final Logger log = LoggerFactory.getLogger(EventMetrics.class);
    private final MeterRegistry registry;

    private final Counter publishedEvents;
    private final Counter consumedEvents;
    private final Counter failedEvents;
    private final Counter retryCount;
    private final Counter deadLetterQueue;
    private final Counter replayCount;
    private final Timer processingTime;
    private final AtomicLong queueLength;

    public EventMetrics(MeterRegistry registry) {
        this.registry = registry;

        this.publishedEvents = Counter.builder("sporekart.events.published.total")
            .description("Total events published").register(registry);
        this.consumedEvents = Counter.builder("sporekart.events.consumed.total")
            .description("Total events consumed").register(registry);
        this.failedEvents = Counter.builder("sporekart.events.failed.total")
            .description("Total failed events").register(registry);
        this.retryCount = Counter.builder("sporekart.events.retry.total")
            .description("Total event retries").register(registry);
        this.deadLetterQueue = Counter.builder("sporekart.events.dlq.total")
            .description("Events moved to DLQ").register(registry);
        this.replayCount = Counter.builder("sporekart.events.replay.total")
            .description("Events replayed").register(registry);

        this.processingTime = Timer.builder("sporekart.events.processing.time")
            .description("Event processing time")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);

        this.queueLength = registry.gauge("sporekart.events.queue.length",
            new AtomicLong(0));

        log.info("Event metrics initialized: 6 counters, 1 timer, 1 gauge");
    }

    public void recordEventPublished(String type) {
        Counter.builder("sporekart.events.published.total")
            .tag("type", type).register(registry).increment();
        publishedEvents.increment();
    }

    public void recordEventConsumed(String type) {
        Counter.builder("sporekart.events.consumed.total")
            .tag("type", type).register(registry).increment();
        consumedEvents.increment();
    }

    public void recordEventFailed(String type, String reason) {
        Counter.builder("sporekart.events.failed.total")
            .tag("type", type).tag("reason", reason).register(registry).increment();
        failedEvents.increment();
    }

    public void recordRetry(String type) {
        Counter.builder("sporekart.events.retry.total")
            .tag("type", type).register(registry).increment();
        retryCount.increment();
    }

    public void recordDLQ(String type) {
        Counter.builder("sporekart.events.dlq.total")
            .tag("type", type).register(registry).increment();
        deadLetterQueue.increment();
    }

    public void recordReplay(String type) {
        Counter.builder("sporekart.events.replay.total")
            .tag("type", type).register(registry).increment();
        replayCount.increment();
    }

    public void recordProcessingTime(long millis) {
        processingTime.record(millis, TimeUnit.MILLISECONDS);
    }

    public void setQueueLength(long length) { queueLength.set(length); }
}
