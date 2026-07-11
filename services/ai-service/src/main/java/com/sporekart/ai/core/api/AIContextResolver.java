package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.CorrelationId;
import java.util.Map;

public interface AIContextResolver {
    Map<String, Object> resolve(AiRequest request, CorrelationId correlationId);
    String resolveUserId(AiRequest request);
    String resolveModule(AiRequest request);
    String resolveProvider(AiRequest request);
}
