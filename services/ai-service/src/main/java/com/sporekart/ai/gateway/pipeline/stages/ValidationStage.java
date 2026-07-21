package com.sporekart.ai.gateway.pipeline.stages;

import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.pipeline.PipelineStage;

public final class ValidationStage {

    private ValidationStage() {}

    public static void execute(PipelineContext context) {
        var request = context.request();
        if (request == null) {
            throw new IllegalArgumentException("Request must not be null");
        }
        if (request.type() == null) {
            throw new IllegalArgumentException("Request type must not be null");
        }
        context.setAttribute("validation.passed", true);
    }

    public static PipelineStage stage() {
        return PipelineStage.VALIDATION;
    }
}
