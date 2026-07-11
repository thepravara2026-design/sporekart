package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.AssistantType;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PromptOrchestrationService {

    public String buildPrompt(AssistantType assistantType, String category, String userMessage,
            Map<String, String> variables) {
        StringBuilder builder = new StringBuilder();
        builder.append("assistant=").append(assistantType.name().toLowerCase()).append("\n");
        builder.append("category=").append(category).append("\n");
        builder.append("user=").append(userMessage).append("\n");
        variables.forEach((key, value) -> builder.append(key).append("=").append(value).append("\n"));
        return builder.toString();
    }
}
