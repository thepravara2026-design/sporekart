package com.sporekart.ai.rag.domain;

import java.util.List;

public record RagContext(
        String query,
        List<RagDocument> documents,
        String assembledContext,
        List<String> citations) {
}
