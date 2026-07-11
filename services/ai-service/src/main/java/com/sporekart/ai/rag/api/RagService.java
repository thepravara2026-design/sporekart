package com.sporekart.ai.rag.api;

import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;
import com.sporekart.ai.rag.domain.RagContext;
import com.sporekart.ai.rag.domain.RagDocument;
import java.util.List;

public interface RagService {
    AiResponse generateWithContext(AiRequest request, RagContext context);
    RagContext buildContext(String query, List<String> documentIds);
    List<RagDocument> retrieve(String query, int maxResults);
}
