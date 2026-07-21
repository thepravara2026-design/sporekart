package com.sporekart.ai.pipeline.middleware;

import com.sporekart.ai.pipeline.PipelineContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimitingMiddleware implements Middleware {
    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> windowStart = new ConcurrentHashMap<>();
    private final int maxRequestsPerMinute;
    private final long windowMs = 60_000;

    public RateLimitingMiddleware() {
        this(100);
    }

    public RateLimitingMiddleware(int maxRequestsPerMinute) {
        this.maxRequestsPerMinute = maxRequestsPerMinute;
    }

    @Override
    public String name() { return "RateLimiting"; }

    @Override
    public int order() { return 30; }

    @Override
    public void execute(PipelineContext context, MiddlewareChain chain) {
        var tenantId = context.request().tenantId();
        var now = System.currentTimeMillis();

        windowStart.compute(tenantId, (key, start) -> {
            if (start == null || (now - start) > windowMs) {
                requestCounts.put(key, new AtomicInteger(0));
                return now;
            }
            return start;
        });

        var count = requestCounts.get(tenantId).incrementAndGet();
        if (count > maxRequestsPerMinute) {
            context.fail("Rate limit exceeded for tenant: " + tenantId);
            context.setAttribute("rateLimitExceeded", true);
            return;
        }

        context.setAttribute("rateLimitRemaining", maxRequestsPerMinute - count);
        context.recordMiddleware(name());
        chain.next(context);
    }
}
