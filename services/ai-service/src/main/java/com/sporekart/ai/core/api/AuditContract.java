package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiModule;
import java.util.Map;

public interface AuditContract {
    void recordAccess(String userId, AiModule module, String action);
    void recordEvent(String eventType, AiModule module, Map<String, Object> details);
    void recordFailure(String userId, AiModule module, String action, String reason);
}
