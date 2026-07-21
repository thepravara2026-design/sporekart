package com.sporekart.ai.gateway.router.strategies;

import com.sporekart.ai.gateway.domain.RoutingDecision;
import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.router.ProviderRouterStrategy;

public class CostOptimizedStrategy implements ProviderRouterStrategy {

    @Override
    public RoutingDecision resolve(PipelineContext context) {
        return new RoutingDecision(
            context.executionContext().providerId(),
            context.executionContext().model(),
            name(),
            "Cost-optimized routing (cheapest provider)",
            3,
            false,
            context.executionContext().providerId() != null
        );
    }

    @Override
    public String name() { return "cost-optimized"; }

    @Override
    public int order() { return 3; }

    @Override
    public boolean supports(PipelineContext context) {
        return context.executionContext().providerId() != null;
    }
}
