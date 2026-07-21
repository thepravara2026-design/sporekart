package com.sporekart.ai.pipeline.middleware;

import com.sporekart.ai.pipeline.PipelineContext;

public class AuthenticationMiddleware implements Middleware {
    @Override
    public String name() { return "Authentication"; }

    @Override
    public int order() { return 10; }

    @Override
    public void execute(PipelineContext context, MiddlewareChain chain) {
        var request = context.request();
        if (request.userId() == null || request.userId().isBlank()) {
            context.fail("Authentication failed: userId is required");
            return;
        }
        if (request.tenantId() == null || request.tenantId().isBlank()) {
            context.fail("Authentication failed: tenantId is required");
            return;
        }
        context.setAttribute("authenticated", true);
        context.setAttribute("authTimestamp", System.currentTimeMillis());
        context.recordMiddleware(name());
        chain.next(context);
    }
}
