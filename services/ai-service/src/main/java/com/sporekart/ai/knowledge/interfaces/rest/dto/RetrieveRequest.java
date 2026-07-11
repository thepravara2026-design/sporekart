package com.sporekart.ai.knowledge.interfaces.rest.dto;

import java.util.List;

public record RetrieveRequest(
        String query,
        List<String> categories,
        String language,
        String visibility,
        String businessModule,
        int maxChunks) {}
