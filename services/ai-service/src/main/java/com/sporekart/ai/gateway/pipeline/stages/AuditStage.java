package com.sporekart.ai.gateway.pipeline.stages;

import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.pipeline.PipelineStage;

public final class AuditStage {

    private AuditStage() {}

    public static void execute(PipelineContext context) {
        context.setAttribute("audit.recorded", true);
        context.setAttribute("audit.pipelineId", context.pipelineId());
    }

    public static PipelineStage stage() {
        return PipelineStage.AUDIT;
    }
}
