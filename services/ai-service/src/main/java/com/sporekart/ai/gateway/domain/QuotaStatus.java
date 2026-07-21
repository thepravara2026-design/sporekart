package com.sporekart.ai.gateway.domain;

public record QuotaStatus(
    boolean available,
    int remaining,
    int limit,
    String period,
    String metric
) {
    public static QuotaStatus available(int remaining, int limit, String period) {
        return new QuotaStatus(true, remaining, limit, period, "tokens");
    }

    public static QuotaStatus exhausted(int limit, String period) {
        return new QuotaStatus(false, 0, limit, period, "tokens");
    }
}
