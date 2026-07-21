package com.sporekart.ai.gateway.pipeline.stages;

import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.pipeline.PipelineStage;

public final class RateLimiterStage {

    private RateLimiterStage() {}

    public static void execute(PipelineContext context) {
        context.setAttribute("rateLimiter.checked", true);
        context.setAttribute("rateLimiter.allowed", true);
    }

    public static PipelineStage stage() {
        return PipelineStage.RATE_LIMITER;
    }
}
