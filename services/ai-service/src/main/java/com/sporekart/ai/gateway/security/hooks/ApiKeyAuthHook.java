package com.sporekart.ai.gateway.security.hooks;

import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.security.SecurityHook;

public class ApiKeyAuthHook implements SecurityHook {

    @Override
    public boolean onAuthenticate(PipelineContext context) {
        var apiKey = context.executionContext().getHeader("X-API-Key");
        context.setAttribute("auth.method", "api-key");
        context.setAttribute("auth.apiKey.present", apiKey.isPresent());
        return apiKey.isPresent();
    }

    @Override
    public boolean onAuthorize(PipelineContext context) {
        return true;
    }

    @Override
    public void onSecurityAudit(PipelineContext context) {
        context.setAttribute("audit.security.method", "api-key");
    }

    @Override
    public void onSecurityViolation(PipelineContext context) {
        context.setAttribute("security.violation", "Invalid API key");
    }

    @Override
    public String name() { return "api-key-auth"; }

    @Override
    public int order() { return 1; }
}
