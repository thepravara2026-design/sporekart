package com.sporekart.ai.memory.domain;

public record MemoryStats(
    long totalMemories,
    long shortTermCount,
    long longTermCount,
    long episodicCount,
    long semanticCount,
    long proceduralCount,
    double avgImportance,
    int activeSessions,
    long storageSizeBytes
) {}
