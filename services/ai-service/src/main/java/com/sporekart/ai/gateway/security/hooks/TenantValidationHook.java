package com.sporekart.ai.gateway.security.hooks;

import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.security.SecurityHook;

public class TenantValidationHook implements SecurityHook {

    @Override
    public boolean onAuthenticate(PipelineContext context) {
        var tenantId = context.executionContext().tenantId();
        context.setAttribute("auth.tenant.valid", tenantId != null);
        return tenantId != null;
    }

    @Override
    public boolean onAuthorize(PipelineContext context) {
        var tenantId = context.executionContext().tenantId();
        context.setAttribute("authorization.tenant.access", tenantId != null);
        return tenantId != null;
    }

    @Override
    public void onSecurityAudit(PipelineContext context) {
        context.setAttribute("audit.tenant", context.executionContext().tenantId());
    }

    @Override
    public void onSecurityViolation(PipelineContext context) {
        context.setAttribute("security.violation", "Invalid tenant");
    }

    @Override
    public String name() { return "tenant-validation"; }

    @Override
    public int order() { return 3; }
}
