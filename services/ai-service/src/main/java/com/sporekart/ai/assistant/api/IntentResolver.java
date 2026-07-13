package com.sporekart.ai.assistant.api;

import com.sporekart.ai.assistant.domain.AssistantIntent;
import com.sporekart.ai.assistant.domain.IntentResult;

import java.util.List;
import java.util.UUID;

public interface IntentResolver {
    IntentResult resolveIntent(String userInput, List<String> availableIntents);
    AssistantIntent classifyIntent(UUID sessionId, String userInput);
    List<IntentResult> resolveMultiIntent(String userInput, List<String> availableIntents);
}
