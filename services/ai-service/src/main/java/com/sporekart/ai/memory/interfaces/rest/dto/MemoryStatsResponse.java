package com.sporekart.ai.memory.interfaces.rest.dto;

public record MemoryStatsResponse(
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
