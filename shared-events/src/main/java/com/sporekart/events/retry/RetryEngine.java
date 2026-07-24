package com.sporekart.events.retry;

import com.sporekart.events.model.Event;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public interface RetryEngine {
    boolean shouldRetry(String eventId);
    void recordFailure(String eventId, String reason);
    void scheduleRetry(Event event, Runnable retryAction);
    void markResolved(String eventId);
    Optional<RetryRecord> getRecord(String eventId);
    List<RetryRecord> getAllRecords();
    int getRetryCount(String eventId);
    void clear();
}
