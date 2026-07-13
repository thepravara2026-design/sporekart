package com.sporekart.ai.promptregistry.domain;

import java.util.List;

public record PromptComparisonResult(
        String promptId,
        int versionA,
        int versionB,
        List<String> differences) {
}
