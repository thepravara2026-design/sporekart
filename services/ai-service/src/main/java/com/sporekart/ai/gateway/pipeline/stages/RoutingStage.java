package com.sporekart.ai.gateway.pipeline.stages;

import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.pipeline.PipelineStage;

public final class RoutingStage {

    private RoutingStage() {}

    public static void execute(PipelineContext context) {
        context.setAttribute("routing.provider", context.executionContext().providerId());
        context.setAttribute("routing.model", context.executionContext().model());
    }

    public static PipelineStage stage() {
        return PipelineStage.ROUTING;
    }
}
