package com.sporekart.ai.pipeline.middleware;

import com.sporekart.ai.pipeline.PipelineContext;
import com.sporekart.ai.pipeline.validation.RequestValidator;

public class ValidationMiddleware implements Middleware {
    private final RequestValidator validator;

    public ValidationMiddleware(RequestValidator validator) {
        this.validator = validator;
    }

    @Override
    public String name() { return "Validation"; }

    @Override
    public int order() { return 1; }

    @Override
    public void execute(PipelineContext context, MiddlewareChain chain) {
        var result = validator.validate(context.request());
        context.setAttribute("validationWarnings", result.warnings());
        if (!result.valid()) {
            context.fail("Request validation failed: " + String.join("; ", result.errors()));
            context.setAttribute("validationErrors", result.errors());
            return;
        }
        context.recordMiddleware(name());
        chain.next(context);
    }
}
