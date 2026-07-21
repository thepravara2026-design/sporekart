package com.sporekart.ai.gateway.pipeline.stages;

import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.pipeline.PipelineStage;

public final class ExecutionStage {

    private ExecutionStage() {}

    public static void execute(PipelineContext context) {
        context.setAttribute("execution.started", true);
        context.setAttribute("execution.completed", false);
    }

    public static void complete(PipelineContext context) {
        context.setAttribute("execution.completed", true);
    }

    public static PipelineStage stage() {
        return PipelineStage.EXECUTION;
    }
}
