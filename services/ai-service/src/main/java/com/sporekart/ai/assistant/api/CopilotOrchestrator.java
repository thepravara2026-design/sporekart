package com.sporekart.ai.assistant.api;

import com.sporekart.ai.assistant.domain.*;

import java.util.Map;

public interface CopilotOrchestrator {
    AssistantResponse handleRequest(AssistantSession session, String userInput, AssistantIntent intent);
    CopilotType resolveCopilot(String intent);
    AssistantExecution executeAction(CopilotType copilot, String action, Map<String, Object> params);
}
