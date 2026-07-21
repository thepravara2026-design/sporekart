package com.sporekart.ai.gateway.contract.message;

import java.util.Map;

public record ToolCall(
    String id,
    String type,
    String functionName,
    Map<String, Object> arguments
) {}
