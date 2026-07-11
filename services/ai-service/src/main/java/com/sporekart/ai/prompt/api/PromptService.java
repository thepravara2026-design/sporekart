package com.sporekart.ai.prompt.api;

import com.sporekart.ai.prompt.domain.PromptTemplate;
import java.util.List;
import java.util.Optional;

public interface PromptService {
    PromptTemplate createTemplate(PromptTemplate template);
    PromptTemplate updateTemplate(PromptTemplate template);
    Optional<PromptTemplate> findById(String id);
    List<PromptTemplate> findByCategory(String category);
    String render(String templateId, java.util.Map<String, Object> variables);
    void deleteTemplate(String id);
}
