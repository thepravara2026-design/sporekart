package com.sporekart.ai.gateway.router;

import com.sporekart.ai.gateway.domain.RoutingDecision;
import com.sporekart.ai.gateway.pipeline.PipelineContext;

public interface ProviderRouterStrategy {
    RoutingDecision resolve(PipelineContext context);
    String name();
    int order();
    boolean supports(PipelineContext context);
}
