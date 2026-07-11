package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;
import com.sporekart.ai.core.domain.CorrelationId;

public interface AIExecutionPipeline {
    AiResponse execute(AiRequest request, CorrelationId correlationId);
    PipelineResult validate(AiRequest request);
    PipelineStatus getStatus(String executionId);

    enum PipelineResult { ACCEPTED, REJECTED, DEFERRED }
    enum PipelineStatus { PENDING, VALIDATING, ROUTING, EXECUTING, COMPLETED, FAILED }
}
