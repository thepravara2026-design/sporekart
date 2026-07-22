package com.sporekart.ai.knowledge.api;

import com.sporekart.ai.knowledge.domain.*;
import java.util.*;

public interface ContextBuilderService {
    RagContext buildContext(String query, List<RetrievalResult> results, int maxTokens);
    RagContext buildContextWithBudget(String query, List<RetrievalResult> results, int maxTokens, boolean deduplicate);
}
