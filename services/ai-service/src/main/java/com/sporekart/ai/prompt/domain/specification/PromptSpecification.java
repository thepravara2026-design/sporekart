package com.sporekart.ai.prompt.domain.specification;

import com.sporekart.ai.prompt.domain.aggregate.Prompt;
import com.sporekart.ai.prompt.domain.valueobject.PromptStatus;

public interface PromptSpecification {
    boolean isSatisfiedBy(Prompt prompt);
    default String errorMessage() { return "Specification not satisfied"; }
}
