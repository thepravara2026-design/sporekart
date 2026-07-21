package com.sporekart.ai.pipeline.retry;

import java.time.Duration;

public record RetryPolicy(
        int maxRetries,
        Duration baseDelay,
        Duration maxDelay,
        double multiplier,
        boolean exponentialBackoff,
        java.util.Set<Class<? extends Throwable>> retryableExceptions) {

    public static final RetryPolicy DEFAULT = new RetryPolicy(
            3, Duration.ofMillis(1000), Duration.ofSeconds(30), 2.0, true, defaultRetryable());

    public static final RetryPolicy NO_RETRY = new RetryPolicy(
            0, Duration.ZERO, Duration.ZERO, 1.0, false, java.util.Set.of());

    public static final RetryPolicy AGGRESSIVE = new RetryPolicy(
            5, Duration.ofMillis(500), Duration.ofSeconds(10), 1.5, true, defaultRetryable());

    public Duration calculateDelay(int attempt) {
        if (attempt <= 0 || maxRetries == 0) return Duration.ZERO;
        if (!exponentialBackoff) return baseDelay;

        long delay = (long) (baseDelay.toMillis() * Math.pow(multiplier, attempt - 1));
        delay = Math.min(delay, maxDelay.toMillis());
        return Duration.ofMillis(delay);
    }

    public boolean shouldRetry(int attempt, Throwable error) {
        if (attempt >= maxRetries) return false;
        if (error == null) return true;
        for (var exceptionType : retryableExceptions) {
            if (exceptionType.isInstance(error)) return true;
        }
        return false;
    }

    private static java.util.Set<Class<? extends Throwable>> defaultRetryable() {
        return java.util.Set.of(
                java.io.IOException.class,
                java.net.SocketTimeoutException.class,
                java.util.concurrent.TimeoutException.class);
    }
}
