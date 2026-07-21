package com.sporekart.ai.pipeline;

import com.sporekart.ai.pipeline.model.PipelineRequest;
import com.sporekart.ai.pipeline.model.PipelineResponse;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PipelineContext {
    private final String pipelineId;
    private final PipelineRequest request;
    private PipelineResponse response;
    private final Map<String, Object> attributes;
    private final Instant startedAt;
    private Instant completedAt;
    private boolean failed;
    private String failureReason;
    private int retryCount;
    private String selectedProvider;
    private String selectedModel;
    private final List<String> middlewareExecuted;
    private Object compiledPrompt;

    public PipelineContext(PipelineRequest request) {
        this.pipelineId = UUID.randomUUID().toString();
        this.request = request;
        this.attributes = new ConcurrentHashMap<>();
        this.startedAt = Instant.now();
        this.failed = false;
        this.retryCount = 0;
        this.middlewareExecuted = new ArrayList<>();
    }

    public String pipelineId() { return pipelineId; }
    public PipelineRequest request() { return request; }
    public PipelineResponse response() { return response; }
    public Map<String, Object> attributes() { return Collections.unmodifiableMap(attributes); }
    public Instant startedAt() { return startedAt; }
    public Instant completedAt() { return completedAt; }
    public boolean failed() { return failed; }
    public String failureReason() { return failureReason; }
    public int retryCount() { return retryCount; }
    public String selectedProvider() { return selectedProvider; }
    public String selectedModel() { return selectedModel; }
    public List<String> middlewareExecuted() { return Collections.unmodifiableList(middlewareExecuted); }
    public Object compiledPrompt() { return compiledPrompt; }

    public void setResponse(PipelineResponse response) {
        this.response = response;
        this.completedAt = Instant.now();
    }

    public void fail(String reason) {
        this.failed = true;
        this.failureReason = reason;
        this.completedAt = Instant.now();
    }

    public void incrementRetry() {
        this.retryCount++;
    }

    public void selectProvider(String provider, String model) {
        this.selectedProvider = provider;
        this.selectedModel = model;
    }

    public void recordMiddleware(String name) {
        this.middlewareExecuted.add(name);
    }

    public void setCompiledPrompt(Object prompt) {
        this.compiledPrompt = prompt;
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        return (T) attributes.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key, T defaultValue) {
        return (T) attributes.getOrDefault(key, defaultValue);
    }

    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    public Duration elapsed() {
        Instant end = completedAt != null ? completedAt : Instant.now();
        return Duration.between(startedAt, end);
    }
}
