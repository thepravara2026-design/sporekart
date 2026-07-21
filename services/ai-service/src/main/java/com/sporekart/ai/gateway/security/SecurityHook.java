package com.sporekart.ai.gateway.security;

import com.sporekart.ai.gateway.pipeline.PipelineContext;

public interface SecurityHook {
    boolean onAuthenticate(PipelineContext context);
    boolean onAuthorize(PipelineContext context);
    void onSecurityAudit(PipelineContext context);
    void onSecurityViolation(PipelineContext context);
    String name();
    int order();
}
