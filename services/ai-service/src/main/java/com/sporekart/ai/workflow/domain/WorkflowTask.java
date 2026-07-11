package com.sporekart.ai.workflow.domain;

import java.util.Map;

public record WorkflowTask(
        String id,
        String type,
        String name,
        String description,
        Map<String, Object> configuration,
        int order,
        boolean required) {
}
