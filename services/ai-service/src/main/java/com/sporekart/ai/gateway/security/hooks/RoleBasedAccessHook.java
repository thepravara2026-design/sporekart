package com.sporekart.ai.gateway.security.hooks;

import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.security.SecurityHook;

public class RoleBasedAccessHook implements SecurityHook {

    @Override
    public boolean onAuthenticate(PipelineContext context) {
        return true;
    }

    @Override
    public boolean onAuthorize(PipelineContext context) {
        var roles = context.executionContext().roles();
        context.setAttribute("authorization.roles", roles);
        context.setAttribute("authorization.required.role", "ai_user");
        return roles != null && !roles.isEmpty();
    }

    @Override
    public void onSecurityAudit(PipelineContext context) {
        context.setAttribute("audit.roles", context.executionContext().roles());
    }

    @Override
    public void onSecurityViolation(PipelineContext context) {
        context.setAttribute("security.violation", "Insufficient permissions");
    }

    @Override
    public String name() { return "role-based-access"; }

    @Override
    public int order() { return 4; }
}
