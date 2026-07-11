package com.sporekart.ai.knowledge.interfaces.rest.dto;

import java.util.List;
import java.util.UUID;

public record RetrieveResponse(
        UUID requestId,
        List<DocumentResponse> documents,
        List<ChunkResponse> chunks,
        List<CitationResponse> citations) {}
