package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.KnowledgeDocument;
import com.sporekart.ai.domain.model.SearchResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SemanticSearchService {
    public List<SearchResult> search(List<KnowledgeDocument> documents, String query) {
        return documents.stream()
                .filter(document -> document.getContent().toLowerCase().contains(query.toLowerCase())
                        || document.getTitle().toLowerCase().contains(query.toLowerCase()))
                .map(document -> new SearchResult(document.getTitle(), document.getContent(), 0.92))
                .collect(Collectors.toList());
    }
}

