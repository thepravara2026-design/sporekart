package com.sporekart.events.dlq;

import java.time.Instant;

public class DeadLetterRecord {
    private final String eventId;
    private final String eventType;
    private final String subscriberName;
    private final String failureReason;
    private final Instant failedAt;
    private final Instant lastReplayedAt;
    private final int replayCount;

    public DeadLetterRecord(String eventId, String eventType, String subscriberName,
                            String failureReason, Instant failedAt) {
        this(eventId, eventType, subscriberName, failureReason, failedAt, null, 0);
    }

    public DeadLetterRecord(String eventId, String eventType, String subscriberName,
                            String failureReason, Instant failedAt,
                            Instant lastReplayedAt, int replayCount) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.subscriberName = subscriberName;
        this.failureReason = failureReason;
        this.failedAt = failedAt;
        this.lastReplayedAt = lastReplayedAt;
        this.replayCount = replayCount;
    }

    public String getEventId() { return eventId; }
    public String getEventType() { return eventType; }
    public String getSubscriberName() { return subscriberName; }
    public String getFailureReason() { return failureReason; }
    public Instant getFailedAt() { return failedAt; }
    public Instant getLastReplayedAt() { return lastReplayedAt; }
    public int getReplayCount() { return replayCount; }

    public DeadLetterRecord withReplayed() {
        return new DeadLetterRecord(eventId, eventType, subscriberName,
                failureReason, failedAt, Instant.now(), replayCount + 1);
    }
}
