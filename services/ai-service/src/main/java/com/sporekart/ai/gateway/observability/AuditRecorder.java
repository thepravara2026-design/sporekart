package com.sporekart.ai.gateway.observability;

import com.sporekart.ai.gateway.pipeline.PipelineContext;

import java.util.Map;

public interface AuditRecorder {
    void record(PipelineContext context, String action);
    void record(String userId, String tenantId, String action, Map<String, Object> details);
    void recordSecurityEvent(String userId, String tenantId, String event, boolean success, String reason);
    void recordProviderCall(String providerId, String model, long durationMs, boolean success);
}
