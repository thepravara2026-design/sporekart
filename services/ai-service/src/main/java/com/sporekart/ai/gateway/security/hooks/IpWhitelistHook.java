package com.sporekart.ai.gateway.security.hooks;

import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.security.SecurityHook;

public class IpWhitelistHook implements SecurityHook {

    @Override
    public boolean onAuthenticate(PipelineContext context) {
        context.setAttribute("auth.ip.whitelist.enabled", false);
        return true;
    }

    @Override
    public boolean onAuthorize(PipelineContext context) {
        return true;
    }

    @Override
    public void onSecurityAudit(PipelineContext context) {
        context.setAttribute("audit.ip.check", "bypassed");
    }

    @Override
    public void onSecurityViolation(PipelineContext context) {
        context.setAttribute("security.violation", "IP not whitelisted");
    }

    @Override
    public String name() { return "ip-whitelist"; }

    @Override
    public int order() { return 5; }
}
