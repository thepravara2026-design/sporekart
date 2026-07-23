package com.sporekart.workspace.domain;

import java.util.List;

public record RoutingRule(
    String ruleId,
    String intentPattern,
    String targetCopilotId,
    double minConfidence,
    int priority,
    List<String> requiredCapabilities,
    String fallbackCopilotId,
    boolean requiresCollaboration,
    List<String> collaboratingCopilotIds
) {}
