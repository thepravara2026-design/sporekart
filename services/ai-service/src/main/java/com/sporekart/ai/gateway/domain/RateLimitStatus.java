package com.sporekart.ai.gateway.domain;

public record RateLimitStatus(
    boolean allowed,
    int remaining,
    int limit,
    long windowSeconds,
    long retryAfterSeconds,
    String reason
) {
    public static RateLimitStatus allowed(int remaining, int limit, long windowSeconds) {
        return new RateLimitStatus(true, remaining, limit, windowSeconds, 0, null);
    }

    public static RateLimitStatus denied(int limit, long windowSeconds, long retryAfter, String reason) {
        return new RateLimitStatus(false, 0, limit, windowSeconds, retryAfter, reason);
    }
}
