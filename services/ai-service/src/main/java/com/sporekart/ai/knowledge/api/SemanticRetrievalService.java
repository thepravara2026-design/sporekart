package com.sporekart.ai.knowledge.api;

import com.sporekart.ai.knowledge.domain.*;
import java.util.*;

public interface SemanticRetrievalService {
    List<RetrievalResult> search(String query, int topK, RetrievalStrategy strategy, String workspaceId, Map<String, String> filters);
    List<RetrievalResult> hybridSearch(String query, int topK, String workspaceId, double vectorWeight, Map<String, String> filters);
    List<RetrievalResult> semanticSearch(String query, int topK, String workspaceId, Map<String, String> filters);
}
