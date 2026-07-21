package com.sporekart.ai.gateway.security.hooks;

import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.security.SecurityHook;

public class AuditLogHook implements SecurityHook {

    @Override
    public boolean onAuthenticate(PipelineContext context) {
        return true;
    }

    @Override
    public boolean onAuthorize(PipelineContext context) {
        return true;
    }

    @Override
    public void onSecurityAudit(PipelineContext context) {
        context.setAttribute("audit.recorded", true);
        context.setAttribute("audit.pipelineId", context.pipelineId());
        context.setAttribute("audit.userId", context.executionContext().userId());
        context.setAttribute("audit.tenantId", context.executionContext().tenantId());
    }

    @Override
    public void onSecurityViolation(PipelineContext context) {
        context.setAttribute("audit.violation.recorded", true);
    }

    @Override
    public String name() { return "audit-log"; }

    @Override
    public int order() { return 7; }
}
