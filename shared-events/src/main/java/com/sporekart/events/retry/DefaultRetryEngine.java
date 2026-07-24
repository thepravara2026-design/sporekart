package com.sporekart.events.retry;

import com.sporekart.events.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultRetryEngine implements RetryEngine {
    private static final Logger log = LoggerFactory.getLogger(DefaultRetryEngine.class);

    private final Map<String, RetryRecord> records = new ConcurrentHashMap<>();
    private final AtomicInteger totalRetries = new AtomicInteger(0);
    private final ScheduledExecutorService scheduler;
    private final RetryPolicy policy;

    public DefaultRetryEngine() {
        this(RetryPolicy.defaultPolicy());
    }

    public DefaultRetryEngine(RetryPolicy policy) {
        this.policy = policy;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "retry-scheduler");
            t.setDaemon(true);
            return t;
        });
    }

    @Override
    public boolean shouldRetry(String eventId) {
        RetryRecord record = records.computeIfAbsent(eventId, RetryRecord::new);
        return record.getAttemptCount() < policy.getMaxRetries() && !record.isResolved();
    }

    @Override
    public void recordFailure(String eventId, String reason) {
        RetryRecord record = records.computeIfAbsent(eventId, RetryRecord::new);
        record.recordAttempt(reason);
        totalRetries.incrementAndGet();
        log.debug("Recorded failure for event {} (attempt {})", eventId, record.getAttemptCount());
    }

    @Override
    public void scheduleRetry(Event event, Runnable retryAction) {
        RetryRecord record = records.computeIfAbsent(event.getEventId().toString(), RetryRecord::new);
        Duration delay = policy.computeDelay(record.getAttemptCount());
        scheduler.schedule(retryAction, delay.toMillis(), TimeUnit.MILLISECONDS);
        log.info("Scheduled retry for event {} in {}ms (attempt {})",
                event.getEventId(), delay.toMillis(), record.getAttemptCount());
    }

    @Override
    public void markResolved(String eventId) {
        RetryRecord record = records.get(eventId);
        if (record != null) {
            record.markResolved();
            log.debug("Marked event {} as resolved", eventId);
        }
    }

    @Override
    public Optional<RetryRecord> getRecord(String eventId) {
        return Optional.ofNullable(records.get(eventId));
    }

    @Override
    public List<RetryRecord> getAllRecords() {
        return List.copyOf(records.values());
    }

    @Override
    public int getRetryCount(String eventId) {
        RetryRecord record = records.get(eventId);
        return record != null ? record.getAttemptCount() : 0;
    }

    @Override
    public void clear() {
        records.clear();
        totalRetries.set(0);
    }

    public int getTotalRetries() {
        return totalRetries.get();
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}
