package com.sporekart.ai.workflow.domain;

import java.util.List;
import java.util.Map;

public record PipelineDefinition(
        String id,
        String name,
        String description,
        List<WorkflowTask> tasks,
        Map<String, Object> defaultInput) {
}
