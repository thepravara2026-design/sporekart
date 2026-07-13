package com.sporekart.ai.semantic.infrastructure.adapters;

import com.sporekart.ai.semantic.api.EmbeddingGenerator;
import com.sporekart.ai.semantic.application.EmbeddingException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProviderRoutingEmbeddingGenerator implements EmbeddingGenerator {

    private static final Logger log = LoggerFactory.getLogger(ProviderRoutingEmbeddingGenerator.class);
    private static final String DEFAULT_PROVIDER = "OPENAI";
    private static final String DEFAULT_MODEL = "text-embedding-3-small";

    private final Map<String, EmbeddingProviderAdapter> adapters = new ConcurrentHashMap<>();

    private final OpenAIEmbeddingAdapter openAIAdapter;
    private final GeminiEmbeddingAdapter geminiAdapter;

    public ProviderRoutingEmbeddingGenerator(OpenAIEmbeddingAdapter openAIAdapter,
                                              GeminiEmbeddingAdapter geminiAdapter) {
        this.openAIAdapter = openAIAdapter;
        this.geminiAdapter = geminiAdapter;
    }

    @PostConstruct
    public void registerAdapters() {
        adapters.put("OPENAI", openAIAdapter);
        adapters.put("GEMINI", geminiAdapter);
        log.info("Registered {} embedding provider adapters", adapters.size());
    }

    @Override
    public List<Double> generate(String content, String provider, String model) {
        String providerKey = provider != null ? provider.toUpperCase() : DEFAULT_PROVIDER;
        EmbeddingProviderAdapter adapter = adapters.get(providerKey);
        if (adapter == null) {
            throw new EmbeddingException("No adapter registered for provider: " + providerKey);
        }
        if (!adapter.isAvailable()) {
            throw new EmbeddingException("Provider " + providerKey + " is not available");
        }
        List<Double> embedding;
        if (adapter instanceof OpenAIEmbeddingAdapter oai && model != null) {
            embedding = oai.generateWithModel(content, model);
        } else {
            embedding = adapter.generate(content);
        }
        log.debug("Generated embedding via {} (model={}, dims={})", providerKey, model, embedding.size());
        return embedding;
    }

    @Override
    public List<List<Double>> generateBatch(List<String> contents, String provider) {
        String providerKey = provider != null ? provider.toUpperCase() : DEFAULT_PROVIDER;
        EmbeddingProviderAdapter adapter = adapters.get(providerKey);
        if (adapter == null) {
            throw new EmbeddingException("No adapter registered for provider: " + providerKey);
        }
        if (!adapter.isAvailable()) {
            throw new EmbeddingException("Provider " + providerKey + " is not available");
        }
        return adapter.generateBatch(contents);
    }
}
