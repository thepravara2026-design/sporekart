package com.sporekart.memory.domain;

import java.time.Duration;

public enum RetentionPolicy {
    STANDARD(Duration.ofDays(90)),
    TEMPORARY(Duration.ofMinutes(30)),
    SHORT(Duration.ofHours(1)),
    SESSION(Duration.ofHours(24)),
    WEEK(Duration.ofDays(7)),
    MONTH(Duration.ofDays(30)),
    QUARTER(Duration.ofDays(90)),
    YEAR(Duration.ofDays(365)),
    FOREVER(null);

    private final Duration duration;

    RetentionPolicy(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() { return duration; }
    public boolean isExpirable() { return duration != null; }
}
