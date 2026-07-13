package com.sporekart.ai.semantic.infrastructure.adapters;

import java.util.List;

public abstract class EmbeddingProviderAdapter {

    private final String providerName;
    private final int defaultDimensions;
    private final int maxTokens;
    private boolean available;

    protected EmbeddingProviderAdapter(String providerName, int defaultDimensions, int maxTokens, boolean available) {
        this.providerName = providerName;
        this.defaultDimensions = defaultDimensions;
        this.maxTokens = maxTokens;
        this.available = available;
    }

    public abstract List<Double> generate(String text);

    public abstract List<List<Double>> generateBatch(List<String> texts);

    public String providerName() { return providerName; }

    public int defaultDimensions() { return defaultDimensions; }

    public int maxTokens() { return maxTokens; }

    public boolean isAvailable() { return available; }

    public void setAvailable(boolean available) { this.available = available; }
}
