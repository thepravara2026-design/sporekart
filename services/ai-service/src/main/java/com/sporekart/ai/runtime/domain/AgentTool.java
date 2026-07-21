package com.sporekart.ai.runtime.domain;

import java.util.Map;

public record AgentTool(
    String name,
    String description,
    ToolType type,
    Map<String, Object> parameters,
    boolean required
) {}
