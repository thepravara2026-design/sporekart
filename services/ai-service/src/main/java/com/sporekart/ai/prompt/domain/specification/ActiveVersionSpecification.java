package com.sporekart.ai.prompt.domain.specification;

import com.sporekart.ai.prompt.domain.aggregate.Prompt;

public class ActiveVersionSpecification implements PromptSpecification {
    @Override
    public boolean isSatisfiedBy(Prompt prompt) {
        return prompt.currentVersion().isPresent();
    }

    @Override
    public String errorMessage() {
        return "Prompt must have an active version";
    }
}
