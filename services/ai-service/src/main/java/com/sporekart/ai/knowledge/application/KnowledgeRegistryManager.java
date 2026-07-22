package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.api.KnowledgeCollectionRepository;
import com.sporekart.ai.knowledge.api.KnowledgeRegistryService;
import com.sporekart.ai.knowledge.api.KnowledgeSourceRepository;
import com.sporekart.ai.knowledge.domain.*;
import java.time.Instant;
import java.util.*;

public class KnowledgeRegistryManager implements KnowledgeRegistryService {
    private final KnowledgeSourceRepository sourceRepository;
    private final KnowledgeCollectionRepository collectionRepository;

    public KnowledgeRegistryManager(KnowledgeSourceRepository sourceRepository,
                                    KnowledgeCollectionRepository collectionRepository) {
        this.sourceRepository = sourceRepository;
        this.collectionRepository = collectionRepository;
    }

    @Override
    public KnowledgeSource registerSource(String name, String description, String workspaceId,
                                          String owner, DocumentType sourceType,
                                          String sourceUrl, String language) {
        var id = KnowledgeSourceId.random();
        var now = Instant.now();
        var source = new KnowledgeSource(id, name, description, workspaceId, owner,
                sourceType, sourceUrl, "1.0", DocumentStatus.DRAFT, language,
                new ArrayList<>(), new HashMap<>(), 365, now, now);
        return sourceRepository.save(source);
    }

    @Override
    public KnowledgeSource getSource(KnowledgeSourceId id) {
        return sourceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Knowledge source not found: " + id));
    }

    @Override
    public List<KnowledgeSource> listSources(String workspaceId, DocumentStatus status) {
        var all = workspaceId != null
                ? sourceRepository.findByWorkspaceId(workspaceId)
                : sourceRepository.findAll();
        if (status != null) {
            all = all.stream().filter(s -> s.status() == status).toList();
        }
        return all;
    }

    @Override
    public KnowledgeSource updateSource(KnowledgeSourceId id, String name, String description,
                                        String sourceUrl, String language) {
        var source = getSource(id);
        var meta = new HashMap<>(source.metadata());
        if (name != null) meta.put("updatedName", name);
        if (description != null) meta.put("updatedDescription", description);
        source.updateMetadata(meta);
        return sourceRepository.save(source);
    }

    @Override
    public void archiveSource(KnowledgeSourceId id) {
        var source = getSource(id);
        source.archive();
        sourceRepository.save(source);
    }

    @Override
    public void publishSource(KnowledgeSourceId id) {
        var source = getSource(id);
        source.publish();
        sourceRepository.save(source);
    }

    @Override
    public void deleteSource(KnowledgeSourceId id) {
        var source = getSource(id);
        source.markDeleted();
        sourceRepository.save(source);
    }

    @Override
    public KnowledgeCollection createCollection(String name, String description,
                                                String workspaceId, String owner) {
        var id = KnowledgeCollectionId.random();
        var now = Instant.now();
        var collection = new KnowledgeCollection(id, name, description, workspaceId, owner,
                KnowledgePermission.READ, new ArrayList<>(), new HashMap<>(), now, now);
        return collectionRepository.save(collection);
    }

    @Override
    public KnowledgeCollection getCollection(KnowledgeCollectionId id) {
        return collectionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Collection not found: " + id));
    }

    @Override
    public List<KnowledgeCollection> listCollections(String workspaceId) {
        return workspaceId != null
                ? collectionRepository.findByWorkspaceId(workspaceId)
                : collectionRepository.findAll();
    }

    @Override
    public void deleteCollection(KnowledgeCollectionId id) {
        collectionRepository.delete(id);
    }
}
