package com.sporekart.ai.gateway.security.hooks;

import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.security.SecurityHook;

public class InputSanitizationHook implements SecurityHook {

    @Override
    public boolean onAuthenticate(PipelineContext context) {
        return true;
    }

    @Override
    public boolean onAuthorize(PipelineContext context) {
        context.setAttribute("inputSanitization.enabled", true);
        return true;
    }

    @Override
    public void onSecurityAudit(PipelineContext context) {
        context.setAttribute("audit.inputSanitization", true);
    }

    @Override
    public void onSecurityViolation(PipelineContext context) {
        context.setAttribute("security.violation", "Malicious input detected");
    }

    @Override
    public String name() { return "input-sanitization"; }

    @Override
    public int order() { return 9; }
}
