package com.sporekart.ai.prompt.domain.factory;

import com.sporekart.ai.prompt.domain.aggregate.Prompt;
import com.sporekart.ai.prompt.domain.entity.PromptTemplate;
import com.sporekart.ai.prompt.domain.event.PromptCreatedEvent;
import com.sporekart.ai.prompt.domain.event.PromptVersionCreatedEvent;
import com.sporekart.ai.prompt.domain.valueobject.*;
import java.util.*;

public class PromptFactory {

    public Prompt create(String name, String description, PromptCategory category,
                         PromptType type, String owner, PromptVisibility visibility,
                         PromptScope scope, PromptPriority priority, String createdBy,
                         PromptMetadata metadata, PromptExecutionPolicy executionPolicy,
                         PromptTemplate initialTemplate) {
        var id = PromptId.generate();
        var prompt = new Prompt(id, name, description, category, type, owner,
            PromptStatus.DRAFT, visibility, scope, priority, createdBy,
            metadata, executionPolicy, Set.of(), Set.of(), Map.of(), null, null);

        if (initialTemplate != null) {
            prompt.createInitialVersion(initialTemplate, createdBy);
        }

        return prompt;
    }

    public Prompt createWithVersion(String name, String description, PromptCategory category,
                                     PromptType type, String owner, PromptVisibility visibility,
                                     PromptScope scope, PromptPriority priority, String createdBy,
                                     PromptMetadata metadata, PromptTemplate template) {
        return create(name, description, category, type, owner, visibility, scope,
            priority, createdBy, metadata, PromptExecutionPolicy.defaults(), template);
    }
}
