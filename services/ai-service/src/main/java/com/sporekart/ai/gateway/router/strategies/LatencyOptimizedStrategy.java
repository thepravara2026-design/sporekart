package com.sporekart.ai.gateway.router.strategies;

import com.sporekart.ai.gateway.domain.RoutingDecision;
import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.router.ProviderRouterStrategy;

public class LatencyOptimizedStrategy implements ProviderRouterStrategy {

    @Override
    public RoutingDecision resolve(PipelineContext context) {
        return new RoutingDecision(
            context.executionContext().providerId(),
            context.executionContext().model(),
            name(),
            "Latency-optimized routing (nearest region)",
            2,
            false,
            context.executionContext().providerId() != null
        );
    }

    @Override
    public String name() { return "latency-optimized"; }

    @Override
    public int order() { return 2; }

    @Override
    public boolean supports(PipelineContext context) {
        return context.executionContext().providerId() != null;
    }
}
