package com.sporekart.search.domain.repository;

import com.sporekart.search.domain.model.SearchDocument;
import com.sporekart.search.domain.model.SearchQuery;
import com.sporekart.search.domain.model.SearchResult;

import java.util.List;
import java.util.Optional;

public interface SearchRepositoryPort {
    SearchDocument index(SearchDocument document);

    List<SearchDocument> bulkIndex(List<SearchDocument> documents);

    Optional<SearchDocument> findById(String id);

    SearchResult search(SearchQuery query);

    void deleteById(String id);

    void deleteByEntity(String entityType, String entityId);

    void clearIndex();
}