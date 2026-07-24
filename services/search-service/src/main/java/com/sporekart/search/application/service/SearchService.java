package com.sporekart.search.application.service;

import com.sporekart.search.application.dto.IndexDocumentRequest;
import com.sporekart.search.application.dto.SearchRequest;
import com.sporekart.search.application.dto.SearchResponse;
import com.sporekart.search.common.exception.DocumentNotFoundException;
import com.sporekart.search.domain.model.SearchDocument;
import com.sporekart.search.domain.model.SearchQuery;
import com.sporekart.search.domain.model.SearchResult;
import com.sporekart.search.domain.repository.SearchRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class SearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchService.class);

    private final SearchRepositoryPort searchRepositoryPort;

    public SearchService(SearchRepositoryPort searchRepositoryPort) {
        this.searchRepositoryPort = searchRepositoryPort;
    }

    public SearchDocument indexDocument(IndexDocumentRequest request) {
        SearchDocument document = new SearchDocument(
                UUID.randomUUID().toString(),
                request.entityType(),
                request.entityId(),
                request.title(),
                request.description(),
                request.content(),
                request.tags() != null ? request.tags() : List.of(),
                request.metadata() != null ? request.metadata() : java.util.Map.of(),
                0.0,
                Instant.now(),
                Instant.now());
        SearchDocument indexed = searchRepositoryPort.index(document);
        LOGGER.info("Indexed document {} for entity {}/{}", indexed.getId(), request.entityType(), request.entityId());
        return indexed;
    }

    public SearchResponse search(SearchRequest request) {
        if (request.query() == null || request.query().isBlank()) {
            return new SearchResponse(List.of(), 0, request.page(), request.size(), 0);
        }
        SearchQuery query = new SearchQuery(
                request.query(),
                request.filters(),
                request.page(),
                request.size(),
                request.sortBy(),
                request.sortOrder());
        SearchResult result = searchRepositoryPort.search(query);
        return new SearchResponse(
                result.documents(),
                result.totalResults(),
                result.page(),
                result.size(),
                result.totalPages());
    }

    public void deleteDocument(String id) {
        SearchDocument existing = searchRepositoryPort.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found: " + id));
        searchRepositoryPort.deleteById(existing.getId());
        LOGGER.info("Deleted document {}", id);
    }

    public SearchDocument reindexEntity(String entityType, String entityId, IndexDocumentRequest request) {
        searchRepositoryPort.deleteByEntity(entityType, entityId);
        SearchDocument document = new SearchDocument(
                UUID.randomUUID().toString(),
                request.entityType(),
                request.entityId(),
                request.title(),
                request.description(),
                request.content(),
                request.tags() != null ? request.tags() : List.of(),
                request.metadata() != null ? request.metadata() : java.util.Map.of(),
                0.0,
                Instant.now(),
                Instant.now());
        SearchDocument indexed = searchRepositoryPort.index(document);
        LOGGER.info("Reindexed entity {}/{}", entityType, entityId);
        return indexed;
    }

    public SearchDocument getDocument(String id) {
        return searchRepositoryPort.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found: " + id));
    }
}