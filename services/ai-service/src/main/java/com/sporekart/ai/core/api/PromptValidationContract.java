package com.sporekart.ai.core.api;

import com.sporekart.ai.core.application.validation.ValidationResult;
import com.sporekart.ai.core.domain.AiRequest;

public interface PromptValidationContract {
    ValidationResult validate(AiRequest request);
    boolean containsSensitiveContent(String prompt);
    boolean exceedsMaxLength(String prompt);
}
