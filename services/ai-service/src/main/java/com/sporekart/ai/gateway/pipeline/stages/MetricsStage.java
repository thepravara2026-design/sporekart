package com.sporekart.ai.gateway.pipeline.stages;

import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.pipeline.PipelineStage;

public final class MetricsStage {

    private MetricsStage() {}

    public static void execute(PipelineContext context) {
        context.setAttribute("metrics.recorded", true);
    }

    public static PipelineStage stage() {
        return PipelineStage.METRICS;
    }
}
