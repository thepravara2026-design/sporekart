package com.sporekart.ai.providerregistry.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProviderModelInfoEmbeddable {

    @Column(name = "model_id")
    private String modelId;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "context_window")
    private int contextWindow;

    @Column(name = "max_tokens")
    private int maxTokens;

    @Column(name = "streaming_supported")
    private boolean streamingSupported;

    @Column(name = "tool_calling_supported")
    private boolean toolCallingSupported;

    @Column(name = "embeddings_supported")
    private boolean embeddingsSupported;

    @Column(name = "image_supported")
    private boolean imageSupported;

    @Column(name = "audio_supported")
    private boolean audioSupported;

    public ProviderModelInfoEmbeddable() {}

    public ProviderModelInfoEmbeddable(String modelId, String modelName, int contextWindow, int maxTokens,
                                       boolean streamingSupported, boolean toolCallingSupported,
                                       boolean embeddingsSupported, boolean imageSupported,
                                       boolean audioSupported) {
        this.modelId = modelId;
        this.modelName = modelName;
        this.contextWindow = contextWindow;
        this.maxTokens = maxTokens;
        this.streamingSupported = streamingSupported;
        this.toolCallingSupported = toolCallingSupported;
        this.embeddingsSupported = embeddingsSupported;
        this.imageSupported = imageSupported;
        this.audioSupported = audioSupported;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getContextWindow() {
        return contextWindow;
    }

    public void setContextWindow(int contextWindow) {
        this.contextWindow = contextWindow;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public boolean isStreamingSupported() {
        return streamingSupported;
    }

    public void setStreamingSupported(boolean streamingSupported) {
        this.streamingSupported = streamingSupported;
    }

    public boolean isToolCallingSupported() {
        return toolCallingSupported;
    }

    public void setToolCallingSupported(boolean toolCallingSupported) {
        this.toolCallingSupported = toolCallingSupported;
    }

    public boolean isEmbeddingsSupported() {
        return embeddingsSupported;
    }

    public void setEmbeddingsSupported(boolean embeddingsSupported) {
        this.embeddingsSupported = embeddingsSupported;
    }

    public boolean isImageSupported() {
        return imageSupported;
    }

    public void setImageSupported(boolean imageSupported) {
        this.imageSupported = imageSupported;
    }

    public boolean isAudioSupported() {
        return audioSupported;
    }

    public void setAudioSupported(boolean audioSupported) {
        this.audioSupported = audioSupported;
    }
}
