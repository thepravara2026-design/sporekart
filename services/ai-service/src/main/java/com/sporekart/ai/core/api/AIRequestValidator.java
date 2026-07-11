package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiRequest;

public interface AIRequestValidator {
    void validate(AiRequest request);
    boolean supports(String module);
}
