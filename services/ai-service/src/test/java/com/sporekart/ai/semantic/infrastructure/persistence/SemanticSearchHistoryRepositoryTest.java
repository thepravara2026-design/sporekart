package com.sporekart.ai.semantic.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class SemanticSearchHistoryRepositoryTest {

    @Autowired
    private SemanticSearchHistoryRepository repository;

    private SemanticSearchHistoryEntity createHistory(String query, String searchType, int resultCount) {
        SemanticSearchHistoryEntity entity = new SemanticSearchHistoryEntity();
        entity.setQuery(query);
        entity.setSearchType(searchType);
        entity.setResultCount(resultCount);
        entity.setLatencyMs(100L);
        entity.setCreatedAt(OffsetDateTime.now());
        return repository.save(entity);
    }

    @Test
    void testSaveAndFindBySearchType() {
        createHistory("test query", "SEMANTIC", 5);
        List<SemanticSearchHistoryEntity> results = repository.findBySearchTypeAndIsDeletedFalse("SEMANTIC");
        assertFalse(results.isEmpty());
        assertTrue(results.stream().allMatch(h -> "SEMANTIC".equals(h.getSearchType())));
    }

    @Test
    void testFindByIsDeletedFalse() {
        createHistory("query1", "SEMANTIC", 3);
        createHistory("query2", "HYBRID", 7);

        List<SemanticSearchHistoryEntity> all = repository.findByIsDeletedFalse();
        assertFalse(all.isEmpty());
    }

    @Test
    void testFindByCreatedBy() {
        UUID userId = UUID.randomUUID();
        SemanticSearchHistoryEntity entity = new SemanticSearchHistoryEntity();
        entity.setQuery("user query");
        entity.setSearchType("SEMANTIC");
        entity.setResultCount(2);
        entity.setLatencyMs(50L);
        entity.setCreatedBy(userId);
        entity.setCreatedAt(OffsetDateTime.now());
        repository.save(entity);

        List<SemanticSearchHistoryEntity> userHistory = repository.findByCreatedByAndIsDeletedFalse(userId);
        assertFalse(userHistory.isEmpty());
    }

    @Test
    void testSoftDelete() {
        SemanticSearchHistoryEntity entity = createHistory("delete me", "SEMANTIC", 0);
        entity.setDeleted(true);
        entity.setDeletedAt(OffsetDateTime.now());
        repository.save(entity);

        List<SemanticSearchHistoryEntity> active = repository.findByIsDeletedFalse();
        assertTrue(active.stream().noneMatch(h -> "delete me".equals(h.getQuery())));
    }

    @Test
    void testFindByOrderByCreatedAtDesc() {
        createHistory("first", "SEMANTIC", 1);
        createHistory("second", "HYBRID", 2);
        List<SemanticSearchHistoryEntity> results = repository.findByIsDeletedFalseOrderByCreatedAtDesc();
        assertFalse(results.isEmpty());
    }
}
