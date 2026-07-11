package com.sporekart.ai.prompt.application;

import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PromptSearchService {

    private final PromptTemplateRepository templateRepository;
    private final PromptCategoryRepository categoryRepository;

    public PromptSearchService(PromptTemplateRepository templateRepository,
                               PromptCategoryRepository categoryRepository) {
        this.templateRepository = templateRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<PromptTemplateEntity> search(String query) {
        if (query == null || query.isBlank()) {
            return templateRepository.findByIsDeletedFalse();
        }
        return templateRepository.search(query.trim());
    }

    public List<PromptTemplateEntity> findByCategory(UUID categoryId) {
        return templateRepository.findByCategoryIdAndIsDeletedFalse(categoryId);
    }

    public List<PromptTemplateEntity> findByStatus(String status) {
        return templateRepository.findByStatusAndIsDeletedFalse(status);
    }

    public List<PromptTemplateEntity> findPublished() {
        return templateRepository.findByStatusAndIsDeletedFalse("PUBLISHED");
    }

    public List<PromptCategoryEntity> listCategories() {
        return categoryRepository.findByIsDeletedFalseOrderByDisplayOrder();
    }
}
