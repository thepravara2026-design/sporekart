package com.sporekart.ai.interfaces.rest;

import com.sporekart.ai.application.service.AiPlatformService;
import com.sporekart.ai.application.service.ConversationService;
import com.sporekart.ai.application.service.EmbeddingService;
import com.sporekart.ai.application.service.FeedbackService;
import com.sporekart.ai.application.service.KnowledgeService;
import com.sporekart.ai.application.service.PromptManagementService;
import com.sporekart.ai.application.service.PromptOrchestrationService;
import com.sporekart.ai.application.service.RecommendationService;
import com.sporekart.ai.application.service.SemanticSearchService;
import com.sporekart.ai.common.exception.AssistantException;
import com.sporekart.ai.common.exception.KnowledgeRetrievalException;
import com.sporekart.ai.common.exception.RecommendationException;
import com.sporekart.ai.common.exception.SemanticSearchException;
import com.sporekart.ai.domain.model.AssistantType;
import com.sporekart.ai.domain.model.Conversation;
import com.sporekart.ai.domain.model.ConversationMessage;
import com.sporekart.ai.domain.model.EmbeddingRequest;
import com.sporekart.ai.domain.model.KnowledgeDocument;
import com.sporekart.ai.domain.model.Prompt;
import com.sporekart.ai.domain.model.ProviderType;
import com.sporekart.ai.domain.model.Recommendation;
import com.sporekart.ai.domain.model.SearchResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/ai")
@Tag(name = "AI Platform", description = "AI platform, chat, prompts, knowledge, search, and assistant endpoints")
public class AiController {
    private final AiPlatformService aiPlatformService;
    private final PromptManagementService promptManagementService;
    private final ConversationService conversationService;
    private final KnowledgeService knowledgeService;
    private final EmbeddingService embeddingService;
    private final PromptOrchestrationService promptOrchestrationService;
    private final SemanticSearchService semanticSearchService;
    private final RecommendationService recommendationService;
    private final FeedbackService feedbackService;

    public AiController(AiPlatformService aiPlatformService,
            PromptManagementService promptManagementService,
            ConversationService conversationService,
            KnowledgeService knowledgeService,
            EmbeddingService embeddingService,
            PromptOrchestrationService promptOrchestrationService,
            SemanticSearchService semanticSearchService,
            RecommendationService recommendationService,
            FeedbackService feedbackService) {
        this.aiPlatformService = aiPlatformService;
        this.promptManagementService = promptManagementService;
        this.conversationService = conversationService;
        this.knowledgeService = knowledgeService;
        this.embeddingService = embeddingService;
        this.promptOrchestrationService = promptOrchestrationService;
        this.semanticSearchService = semanticSearchService;
        this.recommendationService = recommendationService;
        this.feedbackService = feedbackService;
    }

    @GetMapping("/providers")
    @Operation(summary = "List available AI providers")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of AI providers"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public List<String> getProviders() {
        return List.of(ProviderType.GEMINI.name(), ProviderType.OPENAI.name(), ProviderType.CLAUDE.name(),
                ProviderType.AZURE_OPENAI.name(), ProviderType.LOCAL.name(), ProviderType.MOCK.name());
    }

    @PostMapping("/providers")
    @Operation(summary = "Register an AI provider")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Provider registered"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public com.sporekart.ai.interfaces.rest.dto.ProviderRegistrationResponse createProvider(
            @RequestBody Map<String, String> payload) {
        return new com.sporekart.ai.interfaces.rest.dto.ProviderRegistrationResponse(
                "registered", payload.getOrDefault("provider", ProviderType.MOCK.name()));
    }

    @GetMapping("/prompts")
    @Operation(summary = "List prompts")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of prompts"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public List<Prompt> getPrompts() {
        return promptManagementService.listPrompts();
    }

    @PostMapping("/prompts")
    @Operation(summary = "Create a prompt")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Prompt created"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Prompt createPrompt(@RequestBody Map<String, String> payload) {
        return promptManagementService.createPrompt(
                payload.getOrDefault("category", "general"),
                payload.getOrDefault("name", "prompt"),
                payload.getOrDefault("template", "hello"),
                payload.getOrDefault("version", "v1"));
    }

    @GetMapping("/conversations")
    @Operation(summary = "List conversations")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of conversations"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public List<Conversation> getConversations() {
        return conversationService.listConversations();
    }

    @PostMapping("/conversations")
    @Operation(summary = "Start a conversation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Conversation started"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Conversation startConversation(@RequestBody Map<String, String> payload) {
        return conversationService.startConversation(payload.getOrDefault("owner", "system"),
                payload.getOrDefault("context", "default"));
    }

    @GetMapping("/chat/history")
    @Operation(summary = "Get chat history")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Chat history"),
        @ApiResponse(responseCode = "400", description = "Invalid session ID", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public List<ConversationMessage> getChatHistory(@RequestParam UUID sessionId) {
        return conversationService.getHistory(sessionId);
    }

    @DeleteMapping("/chat/{sessionId}")
    @Operation(summary = "Delete a chat session")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Chat deleted"),
        @ApiResponse(responseCode = "404", description = "Session not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public com.sporekart.ai.interfaces.rest.dto.DeleteChatResponse deleteChat(@PathVariable UUID sessionId) {
        conversationService.deleteConversation(sessionId);
        return new com.sporekart.ai.interfaces.rest.dto.DeleteChatResponse("deleted", sessionId.toString());
    }

    @GetMapping("/knowledge")
    @Operation(summary = "List knowledge documents")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of knowledge documents"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public List<KnowledgeDocument> getKnowledgeDocuments() {
        return knowledgeService.listDocuments();
    }

    @PostMapping("/knowledge")
    @Operation(summary = "Upload a knowledge document")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Document uploaded"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public KnowledgeDocument uploadKnowledge(@RequestBody Map<String, String> payload) {
        return knowledgeService.uploadDocument(
                payload.getOrDefault("title", "document"),
                payload.getOrDefault("category", "general"),
                payload.getOrDefault("content", ""));
    }

    @PostMapping("/search")
    @Operation(summary = "Semantic search")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results"),
        @ApiResponse(responseCode = "400", description = "Invalid query", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public List<SearchResult> search(@RequestBody Map<String, String> payload) {
        List<KnowledgeDocument> documents = knowledgeService.search(payload.getOrDefault("query", ""),
                payload.getOrDefault("category", ""));
        if (documents.isEmpty()) {
            throw new SemanticSearchException("No matching documents found");
        }
        return semanticSearchService.search(documents, payload.getOrDefault("query", ""));
    }

    @GetMapping("/recommendations")
    @Operation(summary = "Get recommendations by category")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Recommendations"),
        @ApiResponse(responseCode = "400", description = "Invalid category", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public List<Recommendation> recommendations(@RequestParam String category) {
        if (category == null || category.isBlank()) {
            throw new RecommendationException("Category is required");
        }
        return recommendationService.recommend(category);
    }

    @GetMapping("/customer-assistant")
    @Operation(summary = "Customer assistant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Assistant response"),
        @ApiResponse(responseCode = "400", description = "Invalid prompt", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public com.sporekart.ai.interfaces.rest.dto.AssistantResponse customerAssistant(@RequestParam String prompt) {
        if (prompt == null || prompt.isBlank()) {
            throw new AssistantException("Prompt is required");
        }
        String orchestrated = promptOrchestrationService.buildPrompt(AssistantType.CUSTOMER, "support", prompt,
                Map.of("topic", "support"));
        String response = aiPlatformService.handlePrompt(orchestrated, ProviderType.MOCK);
        return new com.sporekart.ai.interfaces.rest.dto.AssistantResponse("customer", response);
    }

    @GetMapping("/grower-assistant")
    @Operation(summary = "Grower assistant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Assistant response"),
        @ApiResponse(responseCode = "400", description = "Invalid prompt", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public com.sporekart.ai.interfaces.rest.dto.AssistantResponse growerAssistant(@RequestParam String prompt) {
        if (prompt == null || prompt.isBlank()) {
            throw new AssistantException("Prompt is required");
        }
        String orchestrated = promptOrchestrationService.buildPrompt(AssistantType.GROWER, "cultivation", prompt,
                Map.of("topic", "growth"));
        String response = aiPlatformService.handlePrompt(orchestrated, ProviderType.MOCK);
        return new com.sporekart.ai.interfaces.rest.dto.AssistantResponse("grower", response);
    }

    @GetMapping("/admin-assistant")
    @Operation(summary = "Admin assistant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Assistant response"),
        @ApiResponse(responseCode = "400", description = "Invalid prompt", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public com.sporekart.ai.interfaces.rest.dto.AssistantResponse adminAssistant(@RequestParam String prompt) {
        if (prompt == null || prompt.isBlank()) {
            throw new AssistantException("Prompt is required");
        }
        String orchestrated = promptOrchestrationService.buildPrompt(AssistantType.ADMIN, "operations", prompt,
                Map.of("topic", "ops"));
        String response = aiPlatformService.handlePrompt(orchestrated, ProviderType.MOCK);
        return new com.sporekart.ai.interfaces.rest.dto.AssistantResponse("admin", response);
    }

    @PostMapping("/feedback")
    @Operation(summary = "Submit feedback")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Feedback submitted"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public com.sporekart.ai.interfaces.rest.dto.FeedbackResponse feedback(@RequestBody Map<String, String> payload) {
        Map<String, String> result = feedbackService.submit(payload.getOrDefault("type", "general"),
                payload.getOrDefault("message", ""));
        return new com.sporekart.ai.interfaces.rest.dto.FeedbackResponse(result.getOrDefault("status", ""),
                result.getOrDefault("message", ""));
    }

    @PostMapping("/chat")
    @Operation(summary = "Chat completion")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Chat response"),
        @ApiResponse(responseCode = "400", description = "Invalid prompt", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public com.sporekart.ai.interfaces.rest.dto.ChatResponse chat(@RequestBody Map<String, String> payload) {
        String prompt = payload.getOrDefault("prompt", "");
        if (prompt.isBlank()) {
            throw new AssistantException("Prompt is required");
        }
        String response = aiPlatformService.handlePrompt(prompt, ProviderType.MOCK);
        return new com.sporekart.ai.interfaces.rest.dto.ChatResponse(response);
    }

    @PostMapping("/embeddings")
    @Operation(summary = "Generate embeddings")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Embeddings generated"),
        @ApiResponse(responseCode = "400", description = "Invalid text", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public com.sporekart.ai.interfaces.rest.dto.EmbeddingResponse createEmbedding(@RequestParam @NotBlank String text) {
        String embedding = embeddingService.generateEmbedding(new EmbeddingRequest(text));
        return new com.sporekart.ai.interfaces.rest.dto.EmbeddingResponse("generated", embedding);
    }

    @GetMapping("/metrics")
    @Operation(summary = "Get service health metrics")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Health metrics"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public com.sporekart.ai.interfaces.rest.dto.HealthResponse getMetrics() {
        return new com.sporekart.ai.interfaces.rest.dto.HealthResponse("ok");
    }
}
