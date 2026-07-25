package com.sporekart.workflow.application.sdk;

import com.sporekart.workflow.domain.model.*;

import java.util.*;

public class WorkflowBuilder {
    private final String name;
    private final String description;
    private final WorkflowType type;
    private final String domain;
    private final String owner;
    private final String version;
    private final List<WorkflowStep> steps = new ArrayList<>();
    private final Map<String, Object> config = new HashMap<>();
    private final Map<String, Object> metadata = new HashMap<>();

    public WorkflowBuilder(String name, String description, WorkflowType type, String domain, String owner) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.domain = domain;
        this.owner = owner;
        this.version = "1.0.0";
    }

    public WorkflowBuilder withVersion(String version) {
        return this;
    }

    public WorkflowBuilder addStep(String name, String stepType, String description, int order, String... dependsOn) {
        steps.add(new WorkflowStep(name, stepType, description, List.of(dependsOn), Map.of(), order));
        return this;
    }

    public WorkflowBuilder addConfig(String key, Object value) {
        config.put(key, value);
        return this;
    }

    public WorkflowBuilder addMetadata(String key, Object value) {
        metadata.put(key, value);
        return this;
    }

    public WorkflowDefinition build() {
        return WorkflowDefinition.create(name, description, type, domain, owner, version,
            List.copyOf(steps), Map.copyOf(config), Map.copyOf(metadata));
    }
}
