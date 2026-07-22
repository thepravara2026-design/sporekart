package com.sporekart.memory.retrieval;

import com.sporekart.memory.retrieval.RetrievedMemory;

import java.util.List;

public record RetrievalContext(
    String query,
    List<RetrievedMemory> memories,
    int totalResults,
    long processingTimeMs,
    String summary
) {}
