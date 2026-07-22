package com.sporekart.memory.dto;

import com.sporekart.memory.retrieval.RetrievedMemory;

import java.util.List;

public record SearchMemoriesResponse(
    String query,
    List<RetrievedMemory> results,
    int totalResults,
    long processingTimeMs
) {}
