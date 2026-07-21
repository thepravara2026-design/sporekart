package com.sporekart.ai.gateway.router.strategies;

import com.sporekart.ai.gateway.domain.RoutingDecision;
import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.router.ProviderRouterStrategy;

import java.util.List;

public class FallbackStrategy implements ProviderRouterStrategy {

    private final List<String> fallbackProviders;

    public FallbackStrategy(List<String> fallbackProviders) {
        this.fallbackProviders = fallbackProviders;
    }

    @Override
    public RoutingDecision resolve(PipelineContext context) {
        for (String provider : fallbackProviders) {
            return new RoutingDecision(
                provider,
                context.executionContext().model(),
                name(),
                "Fallback to: " + provider,
                10,
                true,
                true
            );
        }
        return RoutingDecision.unresolved();
    }

    @Override
    public String name() { return "fallback"; }

    @Override
    public int order() { return 10; }

    @Override
    public boolean supports(PipelineContext context) {
        return !fallbackProviders.isEmpty();
    }
}
