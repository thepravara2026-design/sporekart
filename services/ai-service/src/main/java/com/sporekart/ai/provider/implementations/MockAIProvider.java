package com.sporekart.ai.provider.implementations;

import com.sporekart.ai.provider.interfaces.AIProvider;
import com.sporekart.ai.provider.models.*;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class MockAIProvider implements AIProvider {
    private final String providerId;
    private final String providerName;
    private final String version;
    private final List<String> supportedModels;
    private final List<String> supportedModalities;
    private final ProviderCapabilities capabilities;
    private final ProviderCostInfo costInfo;
    private final ProviderRateLimits rateLimits;
    private final int maxTokens;
    private volatile boolean available = true;

    public MockAIProvider(String providerId, String providerName) {
        this(providerId, providerName, "1.0.0");
    }

    public MockAIProvider(String providerId, String providerName, String version) {
        this.providerId = providerId;
        this.providerName = providerName;
        this.version = version;
        this.supportedModels = defineModels(providerName);
        this.supportedModalities = defineModalities(providerName);
        this.maxTokens = defineMaxTokens(providerName);
        this.capabilities = buildCapabilities(providerName);
        this.costInfo = buildCostInfo(providerName);
        this.rateLimits = buildRateLimits(providerName);
    }

    @Override
    public String providerId() { return providerId; }

    @Override
    public String providerName() { return providerName; }

    @Override
    public String providerVersion() { return version; }

    @Override
    public ProviderResponse generateCompletion(ProviderRequest request) {
        return buildResponse(request, "completion");
    }

    @Override
    public ProviderResponse generateChat(ProviderRequest request) {
        return buildResponse(request, "chat");
    }

    @Override
    public ProviderResponse generateEmbeddings(ProviderRequest request) {
        return ProviderResponse.success(
                "[embedding vector stub]",
                request.model(),
                providerName,
                new ProviderResponse.TokenUsage(0, 0, 0),
                Duration.ofMillis(50));
    }

    @Override
    public ProviderResponse generateStreaming(ProviderRequest request) {
        return buildResponse(request.withStream(true), "stream");
    }

    @Override
    public CompletableFuture<ProviderResponse> generateCompletionAsync(ProviderRequest request) {
        return CompletableFuture.supplyAsync(() -> generateCompletion(request));
    }

    @Override
    public CompletableFuture<ProviderResponse> generateChatAsync(ProviderRequest request) {
        return CompletableFuture.supplyAsync(() -> generateChat(request));
    }

    @Override
    public ProviderHealth checkHealth() {
        return available
                ? ProviderHealth.healthy(providerId, providerName)
                : ProviderHealth.unhealthy(providerId, providerName, "Simulated failure");
    }

    @Override
    public ProviderCapabilities getCapabilities() { return capabilities; }

    @Override
    public List<String> supportedModels() { return supportedModels; }

    @Override
    public List<String> supportedModalities() { return supportedModalities; }

    @Override
    public int maxTokens() { return maxTokens; }

    @Override
    public ProviderCostInfo costInfo() { return costInfo; }

    @Override
    public ProviderRateLimits rateLimits() { return rateLimits; }

    @Override
    public boolean isAvailable() { return available; }

    @Override
    public Optional<String> version() { return Optional.of(version); }

    public void setAvailable(boolean available) { this.available = available; }

    private ProviderResponse buildResponse(ProviderRequest request, String type) {
        return ProviderResponse.success(
                "[Mock " + providerName + " " + type + "] " + request.prompt(),
                request.model() != null ? request.model() : supportedModels.get(0),
                providerName,
                new ProviderResponse.TokenUsage(10, 50, 60),
                Duration.ofMillis(100 + new Random().nextInt(400)));
    }

    private static List<String> defineModels(String name) {
        return switch (name.toUpperCase()) {
            case "OPENAI" -> List.of("gpt-4", "gpt-4-turbo", "gpt-3.5-turbo", "text-embedding-3");
            case "GEMINI" -> List.of("gemini-pro", "gemini-pro-vision", "gemini-ultra");
            case "CLAUDE" -> List.of("claude-3-opus", "claude-3-sonnet", "claude-3-haiku");
            case "AZURE_OPENAI", "AZURE OPENAI" -> List.of("gpt-4", "gpt-4-turbo", "gpt-3.5-turbo");
            case "OLLAMA" -> List.of("llama3", "mistral", "codellama");
            case "GROQ" -> List.of("mixtral-8x7b", "llama3-70b", "gemma-7b");
            case "MISTRAL" -> List.of("mistral-large", "mistral-medium", "mistral-small");
            case "OPENROUTER" -> List.of("gpt-4", "claude-3-opus", "gemini-pro", "mixtral");
            case "AWS_BEDROCK", "AWS BEDROCK", "BEDROCK" -> List.of("anthropic.claude-v2", "amazon.titan-text");
            case "TOGETHER_AI", "TOGETHER AI" -> List.of("mixtral-8x7b", "llama3-70b");
            default -> List.of("model-1", "model-2");
        };
    }

    private static List<String> defineModalities(String name) {
        return switch (name.toUpperCase()) {
            case "GEMINI" -> List.of("text", "chat", "vision", "embeddings");
            case "CLAUDE" -> List.of("text", "chat", "reasoning", "long-context");
            case "OPENAI" -> List.of("text", "chat", "embeddings", "vision", "audio");
            case "GROQ" -> List.of("text", "chat");
            case "OLLAMA" -> List.of("text", "chat", "embeddings");
            case "MISTRAL" -> List.of("text", "chat");
            case "AZURE_OPENAI", "AZURE OPENAI" -> List.of("text", "chat", "embeddings", "vision");
            default -> List.of("text", "chat");
        };
    }

    private static int defineMaxTokens(String name) {
        return switch (name.toUpperCase()) {
            case "CLAUDE" -> 100000;
            case "GEMINI" -> 8192;
            case "OPENAI" -> 8192;
            default -> 4096;
        };
    }

    private static ProviderCapabilities buildCapabilities(String name) {
        var list = new ArrayList<String>();
        var models = defineModels(name);
        var modalities = defineModalities(name);

        list.add("CHAT_COMPLETION");
        list.add("TEXT_GENERATION");

        boolean streaming = true;
        boolean vision = modalities.contains("vision");
        boolean embeddings = modalities.contains("embeddings");
        boolean functionCalling = !"OLLAMA".equalsIgnoreCase(name);
        boolean jsonMode = !"OLLAMA".equalsIgnoreCase(name);
        boolean imageGen = "GEMINI".equalsIgnoreCase(name);
        boolean audio = "OPENAI".equalsIgnoreCase(name);
        boolean video = "GEMINI".equalsIgnoreCase(name);
        boolean reasoning = "CLAUDE".equalsIgnoreCase(name);

        if (streaming) list.add("STREAMING");
        if (vision) list.add("VISION");
        if (embeddings) list.add("EMBEDDINGS");
        if (functionCalling) list.add("FUNCTION_CALLING");
        if (jsonMode) list.add("JSON_MODE");
        if (imageGen) list.add("IMAGE_GENERATION");
        if (audio) list.add("AUDIO");
        if (video) list.add("VIDEO");
        if (reasoning) list.add("REASONING");

        return new ProviderCapabilities(
                name, Set.copyOf(models), Set.copyOf(modalities),
                defineMaxTokens(name), streaming, vision, functionCalling,
                jsonMode, embeddings, imageGen, audio, video, reasoning, list);
    }

    private static ProviderCostInfo buildCostInfo(String name) {
        double inputCost = switch (name.toUpperCase()) {
            case "OPENAI" -> 0.00003;
            case "GEMINI" -> 0.0000025;
            case "CLAUDE" -> 0.000015;
            case "GROQ" -> 0.000001;
            case "OLLAMA" -> 0.0;
            case "MISTRAL" -> 0.00002;
            default -> 0.00001;
        };
        double outputCost = inputCost * 3;
        var tier = switch (name.toUpperCase()) {
            case "OLLAMA" -> ProviderCostInfo.PricingTier.FREE;
            case "GROQ" -> ProviderCostInfo.PricingTier.LOW;
            case "GEMINI", "MISTRAL" -> ProviderCostInfo.PricingTier.MEDIUM;
            case "OPENAI", "CLAUDE" -> ProviderCostInfo.PricingTier.HIGH;
            default -> ProviderCostInfo.PricingTier.CUSTOM;
        };
        return new ProviderCostInfo(name, inputCost, outputCost, "USD", tier);
    }

    private static ProviderRateLimits buildRateLimits(String name) {
        return switch (name.toUpperCase()) {
            case "OLLAMA" -> ProviderRateLimits.unlimited(name);
            case "GROQ" -> ProviderRateLimits.of(name, 60, 30000);
            case "GEMINI" -> ProviderRateLimits.of(name, 60, 100000);
            case "OPENAI" -> ProviderRateLimits.of(name, 500, 100000);
            case "CLAUDE" -> ProviderRateLimits.of(name, 80, 50000);
            default -> ProviderRateLimits.of(name, 30, 10000);
        };
    }
}
