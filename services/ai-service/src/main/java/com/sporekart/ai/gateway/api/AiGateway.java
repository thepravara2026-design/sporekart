package com.sporekart.ai.gateway.api;

import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;

public interface AiGateway {
    AiResponse route(AiRequest request);
    boolean supports(String providerType);
}
