package com.sporekart.search.infrastructure.persistence;

import com.sporekart.search.domain.model.SearchDocument;
import com.sporekart.search.domain.model.SearchQuery;
import com.sporekart.search.domain.model.SearchResult;
import com.sporekart.search.domain.repository.SearchRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemorySearchRepository implements SearchRepositoryPort {

    private final Map<String, SearchDocument> documentsById = new ConcurrentHashMap<>();

    @Override
    public SearchDocument index(SearchDocument document) {
        documentsById.put(document.getId(), document);
        return document;
    }

    @Override
    public List<SearchDocument> bulkIndex(List<SearchDocument> documents) {
        for (SearchDocument doc : documents) {
            documentsById.put(doc.getId(), doc);
        }
        return documents;
    }

    @Override
    public Optional<SearchDocument> findById(String id) {
        return Optional.ofNullable(documentsById.get(id));
    }

    @Override
    public SearchResult search(SearchQuery query) {
        String searchTerm = query.query() != null ? query.query().toLowerCase().trim() : "";
        List<SearchDocument> filtered = documentsById.values().stream()
                .filter(doc -> matches(doc, searchTerm, query.filters()))
                .map(doc -> {
                    double score = computeScore(doc, searchTerm);
                    SearchDocument scored = new SearchDocument(
                            doc.getId(), doc.getEntityType(), doc.getEntityId(),
                            doc.getTitle(), doc.getDescription(), doc.getContent(),
                            doc.getTags(), doc.getMetadata(), score,
                            doc.getIndexedAt(), doc.getUpdatedAt());
                    return scored;
                })
                .sorted(Comparator.comparingDouble(SearchDocument::getScore).reversed())
                .collect(Collectors.toList());

        long totalResults = filtered.size();
        int fromIndex = query.page() * query.size();
        int toIndex = Math.min(fromIndex + query.size(), filtered.size());
        List<SearchDocument> page = fromIndex >= filtered.size() ? List.of() : filtered.subList(fromIndex, toIndex);
        int totalPages = (int) Math.ceil((double) totalResults / query.size());

        return new SearchResult(page, totalResults, query.page(), query.size(), totalPages);
    }

    @Override
    public void deleteById(String id) {
        documentsById.remove(id);
    }

    @Override
    public void deleteByEntity(String entityType, String entityId) {
        documentsById.values().removeIf(doc ->
                doc.getEntityType().equals(entityType) && doc.getEntityId().equals(entityId));
    }

    @Override
    public void clearIndex() {
        documentsById.clear();
    }

    private boolean matches(SearchDocument doc, String searchTerm, Map<String, String> filters) {
        if (searchTerm.isEmpty()) {
            return false;
        }
        boolean textMatch = false;
        if (doc.getTitle() != null && doc.getTitle().toLowerCase().contains(searchTerm)) {
            textMatch = true;
        }
        if (!textMatch && doc.getDescription() != null && doc.getDescription().toLowerCase().contains(searchTerm)) {
            textMatch = true;
        }
        if (!textMatch && doc.getContent() != null && doc.getContent().toLowerCase().contains(searchTerm)) {
            textMatch = true;
        }
        if (!textMatch && doc.getTags() != null) {
            for (String tag : doc.getTags()) {
                if (tag.toLowerCase().contains(searchTerm)) {
                    textMatch = true;
                    break;
                }
            }
        }
        if (!textMatch) {
            return false;
        }
        if (filters != null && !filters.isEmpty()) {
            for (Map.Entry<String, String> filter : filters.entrySet()) {
                String key = filter.getKey();
                String value = filter.getValue();
                if ("entityType".equals(key) && !doc.getEntityType().equals(value)) {
                    return false;
                }
                if ("entityId".equals(key) && !doc.getEntityId().equals(value)) {
                    return false;
                }
                if (doc.getMetadata() != null && doc.getMetadata().containsKey(key)) {
                    if (!value.equals(doc.getMetadata().get(key))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private double computeScore(SearchDocument doc, String searchTerm) {
        double score = 0.0;
        if (doc.getTitle() != null && doc.getTitle().toLowerCase().contains(searchTerm)) {
            score += 10.0;
        }
        if (doc.getDescription() != null && doc.getDescription().toLowerCase().contains(searchTerm)) {
            score += 5.0;
        }
        if (doc.getContent() != null && doc.getContent().toLowerCase().contains(searchTerm)) {
            score += 1.0;
        }
        if (doc.getTags() != null) {
            for (String tag : doc.getTags()) {
                if (tag.toLowerCase().contains(searchTerm)) {
                    score += 3.0;
                    break;
                }
            }
        }
        return score;
    }
}