package com.sporekart.ai.rag.api;

import com.sporekart.ai.rag.domain.RagDocument;
import java.util.List;

public interface CitationGenerator {
    List<String> generateCitations(List<RagDocument> sources);
}
