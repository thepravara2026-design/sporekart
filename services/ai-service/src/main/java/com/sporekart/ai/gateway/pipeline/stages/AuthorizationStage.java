package com.sporekart.ai.gateway.pipeline.stages;

import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.pipeline.PipelineStage;

public final class AuthorizationStage {

    private AuthorizationStage() {}

    public static void execute(PipelineContext context) {
        var executionContext = context.executionContext();
        context.setAttribute("authorization.tenantId", executionContext.tenantId());
        context.setAttribute("authorization.roles", executionContext.roles());
        context.setAttribute("authorization.authorized", true);
    }

    public static PipelineStage stage() {
        return PipelineStage.AUTHORIZATION;
    }
}
