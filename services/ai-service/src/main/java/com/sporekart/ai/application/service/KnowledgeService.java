package com.sporekart.ai.application.service;

import com.sporekart.ai.common.exception.KnowledgeRetrievalException;
import com.sporekart.ai.domain.model.KnowledgeDocument;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class KnowledgeService {
    private final List<KnowledgeDocument> documents = new ArrayList<>();

    public KnowledgeDocument uploadDocument(String title, String category, String content) {
        KnowledgeDocument document = new KnowledgeDocument(UUID.randomUUID(), title, category, content);
        documents.add(document);
        return document;
    }

    public List<KnowledgeDocument> listDocuments() {
        return new ArrayList<>(documents);
    }

    public List<KnowledgeDocument> search(String query, String category) {
        if (query == null || query.isBlank()) {
            throw new KnowledgeRetrievalException("Search query is required");
        }
        return documents.stream()
                .filter(document -> category == null || category.isBlank()
                        || document.getCategory().equalsIgnoreCase(category))
                .filter(document -> document.getTitle().toLowerCase().contains(query.toLowerCase())
                        || document.getContent().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }
}

