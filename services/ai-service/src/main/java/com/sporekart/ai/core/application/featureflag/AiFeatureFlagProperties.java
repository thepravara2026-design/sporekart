package com.sporekart.ai.core.application.featureflag;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "sporekart.ai.features")
public class AiFeatureFlagProperties {
    private boolean platformEnabled = true;
    private boolean gatewayEnabled = true;
    private boolean chatEnabled = true;
    private boolean ragEnabled = true;
    private boolean searchEnabled = true;
    private boolean contentEnabled = true;
    private boolean workflowEnabled = true;
    private boolean monitoringEnabled = true;
    private boolean providerGemini = true;
    private boolean providerOpenai = true;
    private boolean providerClaude = true;
    private boolean providerAzureOpenai = true;
    private boolean providerBedrock = false;
    private boolean providerOllama = false;
    private boolean providerMistral = false;
    private boolean providerLocalLlm = false;
    private boolean promptEnabled = true;
    private boolean promptCaching = true;
    private boolean promptAudit = true;
    private boolean requestLogging = true;
    private boolean rateLimiting = true;
    private boolean metrics = true;
    private boolean knowledgeEnabled = true;
    private boolean knowledgeCaching = true;
    private boolean knowledgeAudit = true;
    private boolean semanticEnabled = true;
    private boolean semanticCaching = true;
    private boolean semanticAudit = true;
    private boolean semanticEmbedding = true;
    private boolean semanticIndexing = true;
    private boolean semanticSearch = true;
    private boolean semanticRanking = true;
    private boolean vectorIndexEnabled = true;
    private boolean hybridSearchEnabled = true;
    private boolean conversationEnabled = true;
    private boolean conversationCaching = true;
    private boolean conversationAudit = true;
    private boolean conversationSession = true;
    private boolean conversationMemory = true;
    private boolean conversationStreaming = true;
    private boolean conversationRateLimit = true;
    private boolean conversationMonitoring = true;

    public Map<FeatureFlagName, Boolean> toMap() {
        Map<FeatureFlagName, Boolean> result = new LinkedHashMap<>();
        result.put(FeatureFlagName.AI_PLATFORM_ENABLED, platformEnabled);
        result.put(FeatureFlagName.AI_GATEWAY_ENABLED, gatewayEnabled);
        result.put(FeatureFlagName.AI_PROVIDER_ENABLED, true);
        result.put(FeatureFlagName.AI_CHAT_ENABLED, chatEnabled);
        result.put(FeatureFlagName.AI_RAG_ENABLED, ragEnabled);
        result.put(FeatureFlagName.AI_SEARCH_ENABLED, searchEnabled);
        result.put(FeatureFlagName.AI_CONTENT_ENABLED, contentEnabled);
        result.put(FeatureFlagName.AI_WORKFLOW_ENABLED, workflowEnabled);
        result.put(FeatureFlagName.AI_MONITORING_ENABLED, monitoringEnabled);
        result.put(FeatureFlagName.AI_PROVIDER_GEMINI, providerGemini);
        result.put(FeatureFlagName.AI_PROVIDER_OPENAI, providerOpenai);
        result.put(FeatureFlagName.AI_PROVIDER_CLAUDE, providerClaude);
        result.put(FeatureFlagName.AI_PROVIDER_AZURE_OPENAI, providerAzureOpenai);
        result.put(FeatureFlagName.AI_PROVIDER_BEDROCK, providerBedrock);
        result.put(FeatureFlagName.AI_PROVIDER_OLLAMA, providerOllama);
        result.put(FeatureFlagName.AI_PROVIDER_MISTRAL, providerMistral);
        result.put(FeatureFlagName.AI_PROVIDER_LOCAL_LLM, providerLocalLlm);
        result.put(FeatureFlagName.AI_PROMPT_ENABLED, promptEnabled);
        result.put(FeatureFlagName.AI_PROMPT_CACHING, promptCaching);
        result.put(FeatureFlagName.AI_PROMPT_AUDIT, promptAudit);
        result.put(FeatureFlagName.AI_REQUEST_LOGGING, requestLogging);
        result.put(FeatureFlagName.AI_RATE_LIMITING, rateLimiting);
        result.put(FeatureFlagName.AI_METRICS, metrics);
        result.put(FeatureFlagName.AI_KNOWLEDGE_ENABLED, knowledgeEnabled);
        result.put(FeatureFlagName.AI_KNOWLEDGE_CACHING, knowledgeCaching);
        result.put(FeatureFlagName.AI_KNOWLEDGE_AUDIT, knowledgeAudit);
        result.put(FeatureFlagName.AI_SEMANTIC_ENABLED, semanticEnabled);
        result.put(FeatureFlagName.AI_SEMANTIC_CACHING, semanticCaching);
        result.put(FeatureFlagName.AI_SEMANTIC_AUDIT, semanticAudit);
        result.put(FeatureFlagName.AI_SEMANTIC_EMBEDDING, semanticEmbedding);
        result.put(FeatureFlagName.AI_SEMANTIC_INDEXING, semanticIndexing);
        result.put(FeatureFlagName.AI_SEMANTIC_SEARCH, semanticSearch);
        result.put(FeatureFlagName.AI_SEMANTIC_RANKING, semanticRanking);
        result.put(FeatureFlagName.AI_VECTOR_INDEX_ENABLED, vectorIndexEnabled);
        result.put(FeatureFlagName.AI_HYBRID_SEARCH_ENABLED, hybridSearchEnabled);
        result.put(FeatureFlagName.AI_CONVERSATION_ENABLED, conversationEnabled);
        result.put(FeatureFlagName.AI_CONVERSATION_CACHING, conversationCaching);
        result.put(FeatureFlagName.AI_CONVERSATION_AUDIT, conversationAudit);
        result.put(FeatureFlagName.AI_CONVERSATION_SESSION, conversationSession);
        result.put(FeatureFlagName.AI_CONVERSATION_MEMORY, conversationMemory);
        result.put(FeatureFlagName.AI_CONVERSATION_STREAMING, conversationStreaming);
        result.put(FeatureFlagName.AI_CONVERSATION_RATE_LIMIT, conversationRateLimit);
        result.put(FeatureFlagName.AI_CONVERSATION_MONITORING, conversationMonitoring);
        return result;
    }

    public boolean isPlatformEnabled() { return platformEnabled; }
    public void setPlatformEnabled(boolean platformEnabled) { this.platformEnabled = platformEnabled; }
    public boolean isGatewayEnabled() { return gatewayEnabled; }
    public void setGatewayEnabled(boolean gatewayEnabled) { this.gatewayEnabled = gatewayEnabled; }
    public boolean isChatEnabled() { return chatEnabled; }
    public void setChatEnabled(boolean chatEnabled) { this.chatEnabled = chatEnabled; }
    public boolean isRagEnabled() { return ragEnabled; }
    public void setRagEnabled(boolean ragEnabled) { this.ragEnabled = ragEnabled; }
    public boolean isSearchEnabled() { return searchEnabled; }
    public void setSearchEnabled(boolean searchEnabled) { this.searchEnabled = searchEnabled; }
    public boolean isContentEnabled() { return contentEnabled; }
    public void setContentEnabled(boolean contentEnabled) { this.contentEnabled = contentEnabled; }
    public boolean isWorkflowEnabled() { return workflowEnabled; }
    public void setWorkflowEnabled(boolean workflowEnabled) { this.workflowEnabled = workflowEnabled; }
    public boolean isMonitoringEnabled() { return monitoringEnabled; }
    public void setMonitoringEnabled(boolean monitoringEnabled) { this.monitoringEnabled = monitoringEnabled; }
    public boolean isProviderGemini() { return providerGemini; }
    public void setProviderGemini(boolean providerGemini) { this.providerGemini = providerGemini; }
    public boolean isProviderOpenai() { return providerOpenai; }
    public void setProviderOpenai(boolean providerOpenai) { this.providerOpenai = providerOpenai; }
    public boolean isProviderClaude() { return providerClaude; }
    public void setProviderClaude(boolean providerClaude) { this.providerClaude = providerClaude; }
    public boolean isProviderAzureOpenai() { return providerAzureOpenai; }
    public void setProviderAzureOpenai(boolean providerAzureOpenai) { this.providerAzureOpenai = providerAzureOpenai; }
    public boolean isProviderBedrock() { return providerBedrock; }
    public void setProviderBedrock(boolean providerBedrock) { this.providerBedrock = providerBedrock; }
    public boolean isProviderOllama() { return providerOllama; }
    public void setProviderOllama(boolean providerOllama) { this.providerOllama = providerOllama; }
    public boolean isProviderMistral() { return providerMistral; }
    public void setProviderMistral(boolean providerMistral) { this.providerMistral = providerMistral; }
    public boolean isProviderLocalLlm() { return providerLocalLlm; }
    public void setProviderLocalLlm(boolean providerLocalLlm) { this.providerLocalLlm = providerLocalLlm; }
    public boolean isPromptEnabled() { return promptEnabled; }
    public void setPromptEnabled(boolean promptEnabled) { this.promptEnabled = promptEnabled; }
    public boolean isPromptCaching() { return promptCaching; }
    public void setPromptCaching(boolean promptCaching) { this.promptCaching = promptCaching; }
    public boolean isPromptAudit() { return promptAudit; }
    public void setPromptAudit(boolean promptAudit) { this.promptAudit = promptAudit; }
    public boolean isRequestLogging() { return requestLogging; }
    public void setRequestLogging(boolean requestLogging) { this.requestLogging = requestLogging; }
    public boolean isRateLimiting() { return rateLimiting; }
    public void setRateLimiting(boolean rateLimiting) { this.rateLimiting = rateLimiting; }
    public boolean isMetrics() { return metrics; }
    public void setMetrics(boolean metrics) { this.metrics = metrics; }
    public boolean isKnowledgeEnabled() { return knowledgeEnabled; }
    public void setKnowledgeEnabled(boolean knowledgeEnabled) { this.knowledgeEnabled = knowledgeEnabled; }
    public boolean isKnowledgeCaching() { return knowledgeCaching; }
    public void setKnowledgeCaching(boolean knowledgeCaching) { this.knowledgeCaching = knowledgeCaching; }
    public boolean isKnowledgeAudit() { return knowledgeAudit; }
    public void setKnowledgeAudit(boolean knowledgeAudit) { this.knowledgeAudit = knowledgeAudit; }
    public boolean isSemanticEnabled() { return semanticEnabled; }
    public void setSemanticEnabled(boolean semanticEnabled) { this.semanticEnabled = semanticEnabled; }
    public boolean isSemanticCaching() { return semanticCaching; }
    public void setSemanticCaching(boolean semanticCaching) { this.semanticCaching = semanticCaching; }
    public boolean isSemanticAudit() { return semanticAudit; }
    public void setSemanticAudit(boolean semanticAudit) { this.semanticAudit = semanticAudit; }
    public boolean isSemanticEmbedding() { return semanticEmbedding; }
    public void setSemanticEmbedding(boolean semanticEmbedding) { this.semanticEmbedding = semanticEmbedding; }
    public boolean isSemanticIndexing() { return semanticIndexing; }
    public void setSemanticIndexing(boolean semanticIndexing) { this.semanticIndexing = semanticIndexing; }
    public boolean isSemanticSearch() { return semanticSearch; }
    public void setSemanticSearch(boolean semanticSearch) { this.semanticSearch = semanticSearch; }
    public boolean isSemanticRanking() { return semanticRanking; }
    public void setSemanticRanking(boolean semanticRanking) { this.semanticRanking = semanticRanking; }
    public boolean isVectorIndexEnabled() { return vectorIndexEnabled; }
    public void setVectorIndexEnabled(boolean vectorIndexEnabled) { this.vectorIndexEnabled = vectorIndexEnabled; }
    public boolean isHybridSearchEnabled() { return hybridSearchEnabled; }
    public void setHybridSearchEnabled(boolean hybridSearchEnabled) { this.hybridSearchEnabled = hybridSearchEnabled; }
    public boolean isConversationEnabled() { return conversationEnabled; }
    public void setConversationEnabled(boolean conversationEnabled) { this.conversationEnabled = conversationEnabled; }
    public boolean isConversationCaching() { return conversationCaching; }
    public void setConversationCaching(boolean conversationCaching) { this.conversationCaching = conversationCaching; }
    public boolean isConversationAudit() { return conversationAudit; }
    public void setConversationAudit(boolean conversationAudit) { this.conversationAudit = conversationAudit; }
    public boolean isConversationSession() { return conversationSession; }
    public void setConversationSession(boolean conversationSession) { this.conversationSession = conversationSession; }
    public boolean isConversationMemory() { return conversationMemory; }
    public void setConversationMemory(boolean conversationMemory) { this.conversationMemory = conversationMemory; }
    public boolean isConversationStreaming() { return conversationStreaming; }
    public void setConversationStreaming(boolean conversationStreaming) { this.conversationStreaming = conversationStreaming; }
    public boolean isConversationRateLimit() { return conversationRateLimit; }
    public void setConversationRateLimit(boolean conversationRateLimit) { this.conversationRateLimit = conversationRateLimit; }
    public boolean isConversationMonitoring() { return conversationMonitoring; }
    public void setConversationMonitoring(boolean conversationMonitoring) { this.conversationMonitoring = conversationMonitoring; }
}
