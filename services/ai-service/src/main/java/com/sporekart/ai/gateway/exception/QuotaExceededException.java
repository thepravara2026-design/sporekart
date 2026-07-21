package com.sporekart.ai.gateway.exception;

public class QuotaExceededException extends GatewayException {
    public QuotaExceededException(String metric, int limit, String period) {
        super("QUOTA_EXCEEDED", "Quota exceeded for " + metric + ": " + limit + " per " + period, 429);
    }
}
