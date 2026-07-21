package com.sporekart.ai.gateway.pipeline.stages;

import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.pipeline.PipelineStage;

public final class PostProcessingStage {

    private PostProcessingStage() {}

    public static void execute(PipelineContext context) {
        context.setAttribute("postProcessing.started", true);
    }

    public static PipelineStage stage() {
        return PipelineStage.POST_PROCESSING;
    }
}
