package com.sporekart.copilot.tool;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public record ToolResult(
    boolean success,
    Map<String, Object> data,
    String errorMessage,
    long executionTimeMs
) {
    public ToolResult {
        data = data == null ? Collections.emptyMap() : Collections.unmodifiableMap(Map.copyOf(data));
    }

    public static ToolResult success(Map<String, Object> data, long executionTimeMs) {
        return new ToolResult(true, data, null, executionTimeMs);
    }

    public static ToolResult error(String errorMessage, long executionTimeMs) {
        return new ToolResult(false, Collections.emptyMap(), errorMessage, executionTimeMs);
    }
}
