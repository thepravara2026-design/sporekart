package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;
import com.sporekart.ai.core.domain.CorrelationId;

public interface AIService {
    AiResponse execute(AiRequest request);
    AiResponse execute(AiRequest request, CorrelationId correlationId);
    boolean isAvailable();
    String getServiceName();
}
