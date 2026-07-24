package com.sporekart.events.retry;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class RetryRecord {
    private final String eventId;
    private final List<String> failureReasons;
    private int attemptCount;
    private Instant lastAttempt;
    private boolean resolved;

    public RetryRecord(String eventId) {
        this.eventId = eventId;
        this.failureReasons = new ArrayList<>();
        this.attemptCount = 0;
        this.resolved = false;
    }

    public String getEventId() { return eventId; }
    public int getAttemptCount() { return attemptCount; }
    public List<String> getFailureReasons() { return List.copyOf(failureReasons); }
    public Instant getLastAttempt() { return lastAttempt; }
    public boolean isResolved() { return resolved; }

    public void recordAttempt(String error) {
        this.attemptCount++;
        this.lastAttempt = Instant.now();
        this.failureReasons.add(error);
    }

    public void markResolved() {
        this.resolved = true;
    }
}
