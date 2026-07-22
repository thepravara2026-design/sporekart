package com.sporekart.ai.conversation.domain;

import java.util.Map;

public record ToolCall(String toolName, Map<String, Object> arguments, String result, String status) {

    public ToolCall {
        if (status != null && !status.equals("success") && !status.equals("error")) {
            throw new IllegalArgumentException("Status must be 'success' or 'error', got: " + status);
        }
    }

    public static ToolCall success(String toolName, Map<String, Object> arguments, String result) {
        return new ToolCall(toolName, arguments, result, "success");
    }

    public static ToolCall error(String toolName, Map<String, Object> arguments, String errorMessage) {
        return new ToolCall(toolName, arguments, errorMessage, "error");
    }
}
