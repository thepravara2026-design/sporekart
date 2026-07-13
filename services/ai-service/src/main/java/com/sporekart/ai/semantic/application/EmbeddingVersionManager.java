package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.domain.EmbeddingStatus;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingEntity;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EmbeddingVersionManager {

    private static final Logger log = LoggerFactory.getLogger(EmbeddingVersionManager.class);

    private final SemanticEmbeddingRepository embeddingRepository;

    public EmbeddingVersionManager(SemanticEmbeddingRepository embeddingRepository) {
        this.embeddingRepository = embeddingRepository;
    }

    @Transactional
    public SemanticEmbeddingEntity createNewVersion(UUID embeddingId, String content, String embedding,
                                                     String provider, String model, int dimensions,
                                                     UUID createdBy) {
        SemanticEmbeddingEntity existing = embeddingRepository.findByIdAndIsDeletedFalse(embeddingId)
                .orElseThrow(() -> new EmbeddingException("Embedding not found: " + embeddingId));

        SemanticEmbeddingEntity newVersion = new SemanticEmbeddingEntity();
        newVersion.setContent(content != null ? content : existing.getContent());
        newVersion.setEmbedding(embedding != null ? embedding : existing.getEmbedding());
        newVersion.setProvider(provider != null ? provider : existing.getProvider());
        newVersion.setModel(model != null ? model : existing.getModel());
        newVersion.setDimensions(dimensions > 0 ? dimensions : existing.getDimensions());
        newVersion.setStatus(EmbeddingStatus.PENDING.name());
        newVersion.setVersion(existing.getVersion() + 1);
        newVersion.setCreatedBy(createdBy);
        newVersion.setCreatedAt(OffsetDateTime.now());
        newVersion = embeddingRepository.save(newVersion);

        log.info("Created version {} of embedding {} (id={})", newVersion.getVersion(), embeddingId, newVersion.getId());
        return newVersion;
    }

    @Transactional
    public void deprecateVersion(UUID embeddingId) {
        SemanticEmbeddingEntity entity = embeddingRepository.findByIdAndIsDeletedFalse(embeddingId)
                .orElseThrow(() -> new EmbeddingException("Embedding not found: " + embeddingId));
        entity.setStatus(EmbeddingStatus.DEPRECATED.name());
        embeddingRepository.save(entity);
        log.info("Deprecated embedding version: {} (v{})", embeddingId, entity.getVersion());
    }

    @Transactional
    public void upgradeEmbedding(UUID embeddingId, String newEmbedding, String newModel,
                                  int newDimensions, UUID updatedBy) {
        SemanticEmbeddingEntity entity = embeddingRepository.findByIdAndIsDeletedFalse(embeddingId)
                .orElseThrow(() -> new EmbeddingException("Embedding not found: " + embeddingId));

        SemanticEmbeddingEntity upgraded = new SemanticEmbeddingEntity();
        upgraded.setContent(entity.getContent());
        upgraded.setEmbedding(newEmbedding);
        upgraded.setProvider(entity.getProvider());
        upgraded.setModel(newModel);
        upgraded.setDimensions(newDimensions);
        upgraded.setStatus(EmbeddingStatus.COMPLETED.name());
        upgraded.setVersion(entity.getVersion() + 1);
        upgraded.setCreatedBy(updatedBy);
        upgraded.setCreatedAt(OffsetDateTime.now());
        upgraded = embeddingRepository.save(upgraded);

        entity.setStatus(EmbeddingStatus.DEPRECATED.name());
        entity.setUpdatedBy(updatedBy);
        entity.setUpdatedAt(OffsetDateTime.now());
        embeddingRepository.save(entity);

        log.info("Upgraded embedding {} from v{} to v{} (model: {} -> {}, dims: {} -> {})",
                embeddingId, entity.getVersion(), upgraded.getVersion(),
                entity.getModel(), newModel, entity.getDimensions(), newDimensions);
    }

    public List<SemanticEmbeddingEntity> findDeprecatedEmbeddings() {
        return embeddingRepository.findByStatusAndIsDeletedFalse(EmbeddingStatus.DEPRECATED.name());
    }

    public List<SemanticEmbeddingEntity> findActiveEmbeddings() {
        return embeddingRepository.findByStatusAndIsDeletedFalse(EmbeddingStatus.COMPLETED.name());
    }
}
