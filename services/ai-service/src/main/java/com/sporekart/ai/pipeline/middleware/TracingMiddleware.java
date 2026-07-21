package com.sporekart.ai.pipeline.middleware;

import com.sporekart.ai.pipeline.PipelineContext;

import java.util.ArrayList;

public class TracingMiddleware implements Middleware {
    @Override
    public String name() { return "Tracing"; }

    @Override
    public int order() { return 3; }

    @Override
    public void execute(PipelineContext context, MiddlewareChain chain) {
        context.setAttribute("traceStartTime", System.nanoTime());
        context.setAttribute("traceId", "trace-" + context.pipelineId());
        context.setAttribute("traceSteps", new ArrayList<String>());
        context.recordMiddleware(name());
        chain.next(context);
        var traceSteps = context.getAttribute("traceSteps", new ArrayList<String>());
        traceSteps.add("completed:" + (context.failed() ? "failed" : "success"));
        context.setAttribute("traceSteps", traceSteps);
    }
}
