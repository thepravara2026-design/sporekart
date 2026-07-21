package com.sporekart.ai.pipeline.middleware;

import com.sporekart.ai.pipeline.PipelineContext;
import com.sporekart.ai.pipeline.normalization.ResponseNormalizer;

public class ResponseFormattingMiddleware implements Middleware {
    private final ResponseNormalizer normalizer;

    public ResponseFormattingMiddleware(ResponseNormalizer normalizer) {
        this.normalizer = normalizer;
    }

    @Override
    public String name() { return "ResponseFormatting"; }

    @Override
    public int order() { return 85; }

    @Override
    public void execute(PipelineContext context, MiddlewareChain chain) {
        context.recordMiddleware(name());
        chain.next(context);
        if (context.response() != null && !context.failed()) {
            var normalized = normalizer.normalize(context.response());
            context.setAttribute("normalizedResponse", normalized);
        }
    }
}
