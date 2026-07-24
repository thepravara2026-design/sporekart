package com.sporekart.search.infrastructure.persistence;

import com.sporekart.search.domain.model.SearchDocument;
import com.sporekart.search.domain.model.SearchQuery;
import com.sporekart.search.domain.model.SearchResult;
import com.sporekart.search.domain.repository.SearchRepositoryPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
@Transactional
public class JpaSearchRepositoryAdapter implements SearchRepositoryPort {

    private final SearchJpaRepository jpaRepository;

    public JpaSearchRepositoryAdapter(SearchJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public SearchDocument index(SearchDocument document) {
        return jpaRepository.save(SearchDocumentEntity.fromDomain(document)).toDomain();
    }

    @Override
    public List<SearchDocument> bulkIndex(List<SearchDocument> documents) {
        List<SearchDocumentEntity> entities = documents.stream()
                .map(SearchDocumentEntity::fromDomain)
                .toList();
        return jpaRepository.saveAll(entities).stream()
                .map(SearchDocumentEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<SearchDocument> findById(String id) {
        return jpaRepository.findById(id).map(SearchDocumentEntity::toDomain);
    }

    @Override
    public SearchResult search(SearchQuery query) {
        List<SearchDocument> all = jpaRepository.findAll().stream()
                .map(SearchDocumentEntity::toDomain)
                .filter(doc -> doc.getTitle() != null && doc.getTitle().toLowerCase().contains(query.query().toLowerCase())
                    || doc.getDescription() != null && doc.getDescription().toLowerCase().contains(query.query().toLowerCase())
                    || doc.getContent() != null && doc.getContent().toLowerCase().contains(query.query().toLowerCase()))
                .toList();
        int total = all.size();
        int page = query.page();
        int size = query.size() > 0 ? query.size() : 20;
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, total);
        List<SearchDocument> pageContent = fromIndex >= total ? List.of() : all.subList(fromIndex, toIndex);
        int totalPages = (int) Math.ceil((double) total / size);
        return new SearchResult(pageContent, total, page, size, totalPages);
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void deleteByEntity(String entityType, String entityId) {
        jpaRepository.deleteByEntityTypeAndEntityId(entityType, entityId);
    }

    @Override
    public void clearIndex() {
        jpaRepository.deleteAll();
    }
}
