package com.sporekart.events.retry;

import java.time.Duration;

public class RetryPolicy {
    private final int maxRetries;
    private final Duration initialDelay;
    private final Duration maxDelay;
    private final double backoffMultiplier;

    public RetryPolicy() {
        this(3, Duration.ofMillis(100), Duration.ofSeconds(10), 2.0);
    }

    public RetryPolicy(int maxRetries, Duration initialDelay, Duration maxDelay, double backoffMultiplier) {
        this.maxRetries = maxRetries;
        this.initialDelay = initialDelay;
        this.maxDelay = maxDelay;
        this.backoffMultiplier = backoffMultiplier;
    }

    public int getMaxRetries() { return maxRetries; }
    public Duration getInitialDelay() { return initialDelay; }
    public Duration getMaxDelay() { return maxDelay; }
    public double getBackoffMultiplier() { return backoffMultiplier; }

    public Duration computeDelay(int attempt) {
        long delay = (long) (initialDelay.toMillis() * Math.pow(backoffMultiplier, attempt));
        delay = Math.min(delay, maxDelay.toMillis());
        return Duration.ofMillis(delay);
    }

    public static RetryPolicy defaultPolicy() {
        return new RetryPolicy();
    }

    public static RetryPolicy aggressive() {
        return new RetryPolicy(5, Duration.ofMillis(50), Duration.ofSeconds(5), 2.0);
    }

    public static RetryPolicy conservative() {
        return new RetryPolicy(10, Duration.ofSeconds(1), Duration.ofMinutes(1), 1.5);
    }
}
