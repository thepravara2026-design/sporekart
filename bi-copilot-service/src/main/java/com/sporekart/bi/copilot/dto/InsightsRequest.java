package com.sporekart.bi.copilot.dto;

public record InsightsRequest(
    String category,
    String period,
    int limit,
    boolean actionableOnly
) {}