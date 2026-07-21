package com.sporekart.ai.prompt.domain.specification;

import com.sporekart.ai.prompt.domain.aggregate.Prompt;
import com.sporekart.ai.prompt.domain.valueobject.PromptStatus;

public class PublishedVersionSpecification implements PromptSpecification {
    @Override
    public boolean isSatisfiedBy(Prompt prompt) {
        return prompt.currentVersion()
            .map(v -> !v.isDraft())
            .orElse(false);
    }

    @Override
    public String errorMessage() {
        return "Prompt must have a published version";
    }

    public boolean canBePublished(Prompt prompt) {
        return prompt.status() == PromptStatus.APPROVED
            && prompt.currentVersion().isPresent();
    }
}
