package com.sporekart.ai.workflow.api;

import com.sporekart.ai.workflow.domain.PipelineDefinition;
import com.sporekart.ai.workflow.domain.WorkflowTask;
import java.util.Map;

public interface AiPipeline {
    String getId();
    PipelineDefinition getDefinition();
    Map<String, Object> execute(Map<String, Object> input);
    void cancel();
}
