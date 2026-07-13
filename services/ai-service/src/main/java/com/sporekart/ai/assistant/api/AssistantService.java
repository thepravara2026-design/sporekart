package com.sporekart.ai.assistant.api;

import com.sporekart.ai.assistant.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssistantService {
    AssistantSession startSession(UUID assistantType, UUID userId);
    AssistantResponse processMessage(UUID sessionId, String message);
    Optional<Assistant> findById(UUID id);
    List<Assistant> findAll();
    List<AssistantSession> getSessionHistory(UUID userId);
    AssistantFeedback recordFeedback(AssistantFeedback feedback);
    AssistantMetrics getStatistics(UUID assistantId);
}
