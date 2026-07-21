package com.sporekart.ai.gateway.contract.request;

import com.sporekart.ai.gateway.contract.message.ChatMessage;

import java.util.List;
import java.util.Map;

public record ChatCompletionRequest(
    String model,
    List<ChatMessage> messages,
    Double temperature,
    Integer maxTokens,
    Double topP,
    Double frequencyPenalty,
    Double presencePenalty,
    List<Map<String, Object>> tools,
    String userId
) {}
