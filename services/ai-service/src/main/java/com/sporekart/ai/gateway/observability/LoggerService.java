package com.sporekart.ai.gateway.observability;

import com.sporekart.ai.gateway.pipeline.PipelineContext;

public interface LoggerService {
    void logRequest(PipelineContext context);
    void logResponse(PipelineContext context);
    void logError(PipelineContext context, Throwable error);
    void logSecurityEvent(PipelineContext context, String eventType);
    void logAudit(PipelineContext context, String action);
}
