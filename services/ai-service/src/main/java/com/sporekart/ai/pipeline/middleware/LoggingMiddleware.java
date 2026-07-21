package com.sporekart.ai.pipeline.middleware;

import com.sporekart.ai.pipeline.PipelineContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingMiddleware implements Middleware {
    private static final Logger log = LoggerFactory.getLogger(LoggingMiddleware.class);

    @Override
    public String name() { return "Logging"; }

    @Override
    public int order() { return 5; }

    @Override
    public void execute(PipelineContext context, MiddlewareChain chain) {
        var request = context.request();
        log.info("Pipeline [{}] started: module={}, tenant={}, user={}, correlationId={}",
                context.pipelineId(), request.module(), request.tenantId(),
                request.userId(), request.correlationId());
        context.setAttribute("logRequested", true);
        context.recordMiddleware(name());
        chain.next(context);
        if (context.response() != null) {
            log.info("Pipeline [{}] completed: success={}, latency={}, provider={}",
                    context.pipelineId(), !context.failed(), context.elapsed().toMillis(),
                    context.selectedProvider());
        }
    }
}
