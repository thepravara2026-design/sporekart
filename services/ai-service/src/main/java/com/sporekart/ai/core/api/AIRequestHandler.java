package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;

public interface AIRequestHandler {
    AiResponse handle(AiRequest request);
    boolean canHandle(AiRequest request);
}
