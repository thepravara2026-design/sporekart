package com.sporekart.copilot.tool;

import com.sporekart.copilot.context.CopilotContext;
import com.sporekart.copilot.context.UserContext;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public record ToolExecutionContext(
    String toolId,
    Map<String, Object> parameters,
    UserContext userContext,
    CopilotContext copilotContext
) {
    public ToolExecutionContext {
        Objects.requireNonNull(toolId, "toolId must not be null");
        Objects.requireNonNull(userContext, "userContext must not be null");
        parameters = parameters == null ? Collections.emptyMap() : Collections.unmodifiableMap(Map.copyOf(parameters));
    }
}
