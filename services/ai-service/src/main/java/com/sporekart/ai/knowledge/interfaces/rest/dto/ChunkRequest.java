package com.sporekart.ai.knowledge.interfaces.rest.dto;

public class ChunkRequest {
    private String strategy;
    private int maxChunkSize = 1000;
    private int overlap = 100;

    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
    public int getMaxChunkSize() { return maxChunkSize; }
    public void setMaxChunkSize(int maxChunkSize) { this.maxChunkSize = maxChunkSize; }
    public int getOverlap() { return overlap; }
    public void setOverlap(int overlap) { this.overlap = overlap; }
}
