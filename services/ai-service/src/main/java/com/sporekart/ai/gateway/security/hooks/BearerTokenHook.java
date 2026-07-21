package com.sporekart.ai.gateway.security.hooks;

import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.security.SecurityHook;

public class BearerTokenHook implements SecurityHook {

    @Override
    public boolean onAuthenticate(PipelineContext context) {
        var authHeader = context.executionContext().getHeader("Authorization");
        context.setAttribute("auth.method", "bearer-token");
        context.setAttribute("auth.token.present", authHeader.isPresent());
        return authHeader.isPresent();
    }

    @Override
    public boolean onAuthorize(PipelineContext context) {
        return true;
    }

    @Override
    public void onSecurityAudit(PipelineContext context) {
        context.setAttribute("audit.security.method", "bearer-token");
    }

    @Override
    public void onSecurityViolation(PipelineContext context) {
        context.setAttribute("security.violation", "Invalid or expired bearer token");
    }

    @Override
    public String name() { return "bearer-token"; }

    @Override
    public int order() { return 2; }
}
