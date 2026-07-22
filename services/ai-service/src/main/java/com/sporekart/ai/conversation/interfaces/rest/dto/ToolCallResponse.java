package com.sporekart.ai.conversation.interfaces.rest.dto;

import java.util.Map;

public class ToolCallResponse {
    private final String toolName;
    private final Map<String, Object> arguments;
    private final String result;
    private final String status;

    public ToolCallResponse(String toolName, Map<String, Object> arguments, String result, String status) {
        this.toolName = toolName;
        this.arguments = arguments;
        this.result = result;
        this.status = status;
    }

    public String getToolName() {
        return toolName;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public String getResult() {
        return result;
    }

    public String getStatus() {
        return status;
    }
}
