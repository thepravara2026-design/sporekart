package com.sporekart.ai.prompt.config;

import com.sporekart.ai.prompt.infrastructure.PromptKafkaEventPublisher;
import com.sporekart.ai.prompt.infrastructure.PromptRedisCacheService;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AiPromptConfig {

    private static final Logger log = LoggerFactory.getLogger(AiPromptConfig.class);
    private final PromptCategoryRepository categoryRepository;

    public AiPromptConfig(PromptCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostConstruct
    public void initializeDefaultCategories() {
        List<String> defaultCategories = List.of(
                "Customer Support", "Grower Assistant", "Training",
                "Product Recommendations", "Marketplace", "SEO",
                "Marketing", "Content Generation", "Analytics",
                "ERP", "Internal Assistant", "System Prompt",
                "Developer Prompt");

        for (String catName : defaultCategories) {
            if (!categoryRepository.existsByNameAndIsDeletedFalse(catName)) {
                PromptCategoryEntity category = new PromptCategoryEntity(catName, null, null,
                        defaultCategories.indexOf(catName));
                categoryRepository.save(category);
                log.info("Created default prompt category: {}", catName);
            }
        }
    }
}
