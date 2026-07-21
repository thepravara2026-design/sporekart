package com.sporekart.ai.prompt.domain.specification;

import com.sporekart.ai.prompt.domain.aggregate.Prompt;
import com.sporekart.ai.prompt.domain.valueobject.PromptVariable;
import java.util.Set;
import java.util.stream.Collectors;

public class VariableValidationSpecification implements PromptSpecification {
    private final Set<String> requiredVariables;

    public VariableValidationSpecification(Set<String> requiredVariables) {
        this.requiredVariables = requiredVariables;
    }

    @Override
    public boolean isSatisfiedBy(Prompt prompt) {
        return prompt.currentVersion()
            .map(v -> {
                var existingNames = v.template().variables().stream()
                    .map(PromptVariable::name)
                    .collect(Collectors.toSet());
                return existingNames.containsAll(requiredVariables);
            })
            .orElse(false);
    }

    @Override
    public String errorMessage() {
        return "Required variables are missing: " + String.join(", ", requiredVariables);
    }
}
