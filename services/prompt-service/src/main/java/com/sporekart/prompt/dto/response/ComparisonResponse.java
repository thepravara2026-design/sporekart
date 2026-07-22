package com.sporekart.prompt.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record ComparisonResponse(
        VersionDiff versionA,
        VersionDiff versionB,
        List<String> addedLines,
        List<String> removedLines,
        long tokenDelta,
        BigDecimal costDelta,
        String qualityNotes
) {
    public record VersionDiff(int version, String promptBody, int tokenCount, BigDecimal estimatedCost) {}
}
