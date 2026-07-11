package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;

public interface AIGateway {
    AiResponse execute(AiRequest request);
    boolean isAvailable();
    String getGatewayName();
}
