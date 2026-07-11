package com.sporekart.ai.knowledge.config;

import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeCategoryEntity;
import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeCategoryRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class KnowledgeConfig {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeConfig.class);
    private final KnowledgeCategoryRepository categoryRepository;

    public KnowledgeConfig(KnowledgeCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostConstruct
    public void initializeDefaultCategories() {
        List<String> defaultCategories = List.of(
                "Training", "Product Catalog", "Product Documentation",
                "FAQ", "Policies", "Help Articles",
                "Grower Manuals", "Supplier Documents", "Internal SOPs",
                "Marketing", "Support", "Technical Documentation",
                "Compliance", "Research", "Operations");

        for (int i = 0; i < defaultCategories.size(); i++) {
            String catName = defaultCategories.get(i);
            if (!categoryRepository.existsByNameAndIsDeletedFalse(catName)) {
                KnowledgeCategoryEntity category = new KnowledgeCategoryEntity(catName, null, i);
                categoryRepository.save(category);
                log.info("Created default knowledge category: {}", catName);
            }
        }
    }
}
