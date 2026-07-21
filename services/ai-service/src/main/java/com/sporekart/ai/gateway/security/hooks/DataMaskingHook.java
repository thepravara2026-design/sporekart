package com.sporekart.ai.gateway.security.hooks;

import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.security.SecurityHook;

public class DataMaskingHook implements SecurityHook {

    @Override
    public boolean onAuthenticate(PipelineContext context) {
        return true;
    }

    @Override
    public boolean onAuthorize(PipelineContext context) {
        context.setAttribute("dataMasking.enabled", true);
        return true;
    }

    @Override
    public void onSecurityAudit(PipelineContext context) {
        context.setAttribute("audit.dataMasking", true);
    }

    @Override
    public void onSecurityViolation(PipelineContext context) {
        context.setAttribute("security.violation", "Data masking violation");
    }

    @Override
    public String name() { return "data-masking"; }

    @Override
    public int order() { return 8; }
}
