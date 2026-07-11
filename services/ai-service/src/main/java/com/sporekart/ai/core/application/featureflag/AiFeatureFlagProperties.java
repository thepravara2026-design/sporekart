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
}
