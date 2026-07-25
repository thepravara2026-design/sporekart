package com.sporekart.workflow.domain.model;

import java.util.List;
import java.util.Map;

public record WorkflowStep(
    String name,
    String type,
    String description,
    List<String> dependsOn,
    Map<String, Object> config,
    int order
) {
    public WorkflowStep {
        dependsOn = dependsOn == null ? List.of() : List.copyOf(dependsOn);
        config = config == null ? Map.of() : Map.copyOf(config);
    }
}
