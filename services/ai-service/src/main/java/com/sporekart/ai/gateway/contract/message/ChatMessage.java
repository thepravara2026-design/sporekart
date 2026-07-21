package com.sporekart.ai.gateway.contract.message;

import java.util.Map;

public record ChatMessage(
    String role,
    String content,
    String name,
    Map<String, Object> toolCalls,
    String toolCallId
) {
    public static ChatMessage system(String content) {
        return new ChatMessage("system", content, null, null, null);
    }

    public static ChatMessage user(String content) {
        return new ChatMessage("user", content, null, null, null);
    }

    public static ChatMessage assistant(String content) {
        return new ChatMessage("assistant", content, null, null, null);
    }

    public static ChatMessage tool(String content, String toolCallId) {
        return new ChatMessage("tool", content, null, null, toolCallId);
    }

    public static ChatMessage function(String content, String name) {
        return new ChatMessage("function", content, name, null, null);
    }
}
