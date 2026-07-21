package com.sporekart.ai.gateway.observability;

import com.sporekart.ai.gateway.pipeline.PipelineContext;

import java.util.Map;
import java.util.Optional;

public interface Tracer {
    String startSpan(String operationName, PipelineContext context);
    void endSpan(String spanId);
    void endSpanWithError(String spanId, Throwable error);
    void addEvent(String spanId, String eventName, Map<String, Object> attributes);
    void setAttribute(String spanId, String key, Object value);
    Optional<String> getCurrentSpanId();
}
