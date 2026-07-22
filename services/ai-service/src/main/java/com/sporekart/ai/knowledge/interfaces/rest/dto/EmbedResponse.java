package com.sporekart.ai.knowledge.interfaces.rest.dto;

import java.util.List;

public class EmbedResponse {
    private List<Float> embedding;
    private int dimensions;
    private String provider;

    public List<Float> getEmbedding() { return embedding; }
    public void setEmbedding(List<Float> embedding) { this.embedding = embedding; }
    public int getDimensions() { return dimensions; }
    public void setDimensions(int dimensions) { this.dimensions = dimensions; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
}
