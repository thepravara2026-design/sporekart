package com.sporekart.ai.gateway.security.hooks;

import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.security.SecurityHook;

public class RateLimitSecurityHook implements SecurityHook {

    @Override
    public boolean onAuthenticate(PipelineContext context) {
        return true;
    }

    @Override
    public boolean onAuthorize(PipelineContext context) {
        context.setAttribute("rateLimit.security.enabled", true);
        return true;
    }

    @Override
    public void onSecurityAudit(PipelineContext context) {
        context.setAttribute("audit.rateLimit", true);
    }

    @Override
    public void onSecurityViolation(PipelineContext context) {
        context.setAttribute("security.violation", "Rate limit security threshold exceeded");
    }

    @Override
    public String name() { return "rate-limit-security"; }

    @Override
    public int order() { return 6; }
}
