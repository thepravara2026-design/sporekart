package com.sporekart.ai.rag.api;

import com.sporekart.ai.rag.domain.RagContext;
import com.sporekart.ai.rag.domain.RagDocument;
import java.util.List;

public interface ContextAssembler {
    RagContext assemble(List<RagDocument> documents, String query);
}
