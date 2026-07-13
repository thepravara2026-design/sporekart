package com.sporekart.ai.automation.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface EscalationManager {
    void triggerEscalation(UUID entityId, String entityType, String reason);
    List<Map<String, Object>> getActiveEscalations();
    void resolveEscalation(UUID id);
}
