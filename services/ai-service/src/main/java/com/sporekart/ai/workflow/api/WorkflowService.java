package com.sporekart.ai.workflow.api;

import com.sporekart.ai.workflow.domain.PipelineDefinition;
import com.sporekart.ai.workflow.domain.WorkflowTask;
import java.util.Map;

public interface WorkflowService {
    String executePipeline(PipelineDefinition pipeline, Map<String, Object> input);
    WorkflowTask executeTask(WorkflowTask task, Map<String, Object> context);
    void cancelPipeline(String pipelineId);
    PipelineStatus getPipelineStatus(String pipelineId);
    enum PipelineStatus { PENDING, RUNNING, COMPLETED, FAILED, CANCELLED }
}
