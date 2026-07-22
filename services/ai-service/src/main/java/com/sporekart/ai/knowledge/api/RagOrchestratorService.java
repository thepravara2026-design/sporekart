package com.sporekart.ai.knowledge.api;

import com.sporekart.ai.knowledge.domain.*;
import java.util.*;

public interface RagOrchestratorService {
    RagContext executeQuery(String query, String workspaceId, int topK, RetrievalStrategy strategy, int maxContextTokens, EmbeddingProvider embeddingProvider, Map<String, String> filters);
    List<Citation> getCitations(String query, RetrievalResult result);
}
