package com.sporekart.ai.gateway.pipeline.stages;

import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.pipeline.PipelineStage;

public final class AuthenticationStage {

    private AuthenticationStage() {}

    public static void execute(PipelineContext context) {
        var executionContext = context.executionContext();
        if (executionContext.userId() == null) {
            context.setAttribute("auth.authenticated", false);
            return;
        }
        context.setAttribute("auth.authenticated", true);
        context.setAttribute("auth.userId", executionContext.userId());
    }

    public static PipelineStage stage() {
        return PipelineStage.AUTHENTICATION;
    }
}
