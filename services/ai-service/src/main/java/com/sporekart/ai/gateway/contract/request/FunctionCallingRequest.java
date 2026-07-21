package com.sporekart.ai.gateway.contract.request;

import com.sporekart.ai.gateway.contract.message.ChatMessage;

import java.util.List;
import java.util.Map;

public record FunctionCallingRequest(
    String model,
    List<ChatMessage> messages,
    List<Map<String, Object>> functions,
    String userId
) {}
