package com.sporekart.ai.pipeline.middleware;

import com.sporekart.ai.pipeline.PipelineContext;

import java.util.Set;

public class AuthorizationMiddleware implements Middleware {
    private final Set<String> publicModules = Set.of("chat", "search", "content");
    private final Set<String> restrictedModules = Set.of("admin", "compliance", "audit", "governance");

    @Override
    public String name() { return "Authorization"; }

    @Override
    public int order() { return 20; }

    @Override
    public void execute(PipelineContext context, MiddlewareChain chain) {
        var module = context.request().module();
        if (module == null) {
            context.fail("Authorization failed: module is required");
            return;
        }

        var moduleLower = module.toLowerCase();
        if (restrictedModules.contains(moduleLower)) {
            var userId = context.request().userId();
            if (userId == null || userId.equals("anonymous")) {
                context.fail("Authorization failed: restricted module '" + module + "' requires authenticated user");
                return;
            }
            context.setAttribute("authorizationLevel", "restricted");
        } else if (publicModules.contains(moduleLower)) {
            context.setAttribute("authorizationLevel", "public");
        } else {
            context.setAttribute("authorizationLevel", "standard");
        }
        context.recordMiddleware(name());
        chain.next(context);
    }
}
