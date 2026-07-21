package com.sporekart.ai.gateway.router;

import com.sporekart.ai.gateway.domain.RoutingDecision;
import com.sporekart.ai.gateway.pipeline.PipelineContext;

import java.util.List;
import java.util.Optional;

public interface ProviderRouter {
    RoutingDecision route(PipelineContext context);
    void registerStrategy(ProviderRouterStrategy strategy);
    List<ProviderRouterStrategy> getStrategies();
    Optional<ProviderRouterStrategy> getStrategy(String name);
}
