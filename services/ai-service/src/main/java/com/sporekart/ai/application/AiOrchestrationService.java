package com.sporekart.ai.application;

import com.sporekart.ai.application.service.AiPlatformService;
import com.sporekart.ai.application.service.KnowledgeService;
import com.sporekart.ai.application.service.PromptOrchestrationService;
import com.sporekart.ai.application.service.SemanticSearchService;
import com.sporekart.ai.common.exception.AssistantException;
import com.sporekart.ai.common.exception.SemanticSearchException;
import com.sporekart.ai.domain.model.AssistantType;
import com.sporekart.ai.domain.model.KnowledgeDocument;
import com.sporekart.ai.domain.model.ProviderType;
import com.sporekart.ai.domain.model.SearchResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AiOrchestrationService {

    private final KnowledgeService knowledgeService;
    private final SemanticSearchService semanticSearchService;
    private final PromptOrchestrationService promptOrchestrationService;
    private final AiPlatformService aiPlatformService;

    public AiOrchestrationService(KnowledgeService knowledgeService,
                                   SemanticSearchService semanticSearchService,
                                   PromptOrchestrationService promptOrchestrationService,
                                   AiPlatformService aiPlatformService) {
        this.knowledgeService = knowledgeService;
        this.semanticSearchService = semanticSearchService;
        this.promptOrchestrationService = promptOrchestrationService;
        this.aiPlatformService = aiPlatformService;
    }

    public List<SearchResult> search(String query, String category) {
        List<KnowledgeDocument> documents = knowledgeService.search(query, category);
        if (documents.isEmpty()) {
            throw new SemanticSearchException("No matching documents found");
        }
        return semanticSearchService.search(documents, query);
    }

    public String customerAssistant(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            throw new AssistantException("Prompt is required");
        }
        String orchestrated = promptOrchestrationService.buildPrompt(AssistantType.CUSTOMER, "support", prompt,
                Map.of("topic", "support"));
        return aiPlatformService.handlePrompt(orchestrated, ProviderType.MOCK);
    }

    public String growerAssistant(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            throw new AssistantException("Prompt is required");
        }
        String orchestrated = promptOrchestrationService.buildPrompt(AssistantType.GROWER, "cultivation", prompt,
                Map.of("topic", "growth"));
        return aiPlatformService.handlePrompt(orchestrated, ProviderType.MOCK);
    }

    public String adminAssistant(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            throw new AssistantException("Prompt is required");
        }
        String orchestrated = promptOrchestrationService.buildPrompt(AssistantType.ADMIN, "operations", prompt,
                Map.of("topic", "ops"));
        return aiPlatformService.handlePrompt(orchestrated, ProviderType.MOCK);
    }

    public String chat(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            throw new AssistantException("Prompt is required");
        }
        return aiPlatformService.handlePrompt(prompt, ProviderType.MOCK);
    }

    public String registerProvider(String providerName) {
        return providerName;
    }

    public String health() {
        return "ok";
    }
}
