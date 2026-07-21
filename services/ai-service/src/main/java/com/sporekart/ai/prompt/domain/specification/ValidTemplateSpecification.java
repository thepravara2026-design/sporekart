package com.sporekart.ai.prompt.domain.specification;

import com.sporekart.ai.prompt.domain.aggregate.Prompt;

public class ValidTemplateSpecification implements PromptSpecification {
    @Override
    public boolean isSatisfiedBy(Prompt prompt) {
        return prompt.currentVersion()
            .map(v -> v.template() != null && v.template().isValid())
            .orElse(false);
    }

    @Override
    public String errorMessage() {
        return "Prompt must have a valid template";
    }
}
