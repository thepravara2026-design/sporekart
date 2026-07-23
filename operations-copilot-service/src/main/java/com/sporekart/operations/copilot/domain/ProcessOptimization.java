package com.sporekart.operations.copilot.domain;

import java.util.List;

public record ProcessOptimization(
    String optId,
    String processName,
    String currentMethod,
    String suggestedMethod,
    double estimatedSavings,
    double implementationCost,
    double efficiencyGain,
    String timeToImplement,
    List<String> requiredChanges,
    String riskLevel
) {}
