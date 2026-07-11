package com.sporekart.ai.gateway.api;

import com.sporekart.ai.core.domain.AiRequest;

public interface RequestValidator {
    void validate(AiRequest request);
}
