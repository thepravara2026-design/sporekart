package com.sporekart.ai.prompt.api;

import com.sporekart.ai.prompt.domain.PromptTemplate;
import java.util.List;
import java.util.Optional;

public interface PromptTemplatePort {
    PromptTemplate save(PromptTemplate template);
    Optional<PromptTemplate> findById(String id);
    List<PromptTemplate> findAll();
    List<PromptTemplate> findByCategory(String category);
    void deleteById(String id);
}
