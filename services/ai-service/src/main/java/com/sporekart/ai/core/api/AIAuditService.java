package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiModule;
import java.util.Map;

public interface AIAuditService {
    void recordRequestReceived(String executionId, String correlationId, AiModule module, String userId);
    void recordRequestCompleted(String executionId, String correlationId, AiModule module, boolean success, long durationMs);
    void recordRequestRejected(String executionId, String correlationId, AiModule module, String reason);
    void recordEvent(String eventType, AiModule module, Map<String, Object> details);
}
