package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.CorrelationId;
import java.util.Map;

public interface StructuredLogger {
    void info(String message, CorrelationId correlationId, Map<String, Object> context);
    void warn(String message, CorrelationId correlationId, Map<String, Object> context);
    void error(String message, CorrelationId correlationId, Map<String, Object> context, Throwable error);
    void debug(String message, CorrelationId correlationId, Map<String, Object> context);
}
