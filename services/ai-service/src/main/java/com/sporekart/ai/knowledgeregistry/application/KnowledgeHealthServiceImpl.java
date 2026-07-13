package com.sporekart.ai.knowledgeregistry.application;

import com.sporekart.ai.knowledgeregistry.api.KnowledgeHealthService;
import com.sporekart.ai.knowledgeregistry.domain.SourceHealthStatus;
import com.sporekart.ai.knowledgeregistry.infrastructure.persistence.KnowledgeSourceEntity;
import com.sporekart.ai.knowledgeregistry.infrastructure.persistence.KnowledgeSourceHealthEntity;
import com.sporekart.ai.knowledgeregistry.infrastructure.persistence.KnowledgeSourceHealthRepository;
import com.sporekart.ai.knowledgeregistry.infrastructure.persistence.KnowledgeSourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class KnowledgeHealthServiceImpl implements KnowledgeHealthService {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeHealthServiceImpl.class);

    private final KnowledgeSourceHealthRepository healthRepository;
    private final KnowledgeSourceRepository sourceRepository;

    public KnowledgeHealthServiceImpl(KnowledgeSourceHealthRepository healthRepository,
                                      KnowledgeSourceRepository sourceRepository) {
        this.healthRepository = healthRepository;
        this.sourceRepository = sourceRepository;
    }

    @Override
    @Transactional
    public KnowledgeSourceHealthEntity recordHealth(String sourceId, SourceHealthStatus status) {
        KnowledgeSourceEntity source = sourceRepository.findById(sourceId)
                .orElseThrow(() -> new IllegalArgumentException("Knowledge source not found: " + sourceId));
        Instant now = Instant.now();
        KnowledgeSourceHealthEntity health = new KnowledgeSourceHealthEntity();
        health.setSourceId(sourceId);
        health.setStatus(status);
        health.setCheckedAt(now);
        KnowledgeSourceHealthEntity saved = healthRepository.save(health);
        source.setHealthStatus(status);
        source.setUpdatedAt(now);
        sourceRepository.save(source);
        log.info("Recorded health {} for knowledge source: {}", status, sourceId);
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public SourceHealthStatus getHealth(String sourceId) {
        KnowledgeSourceEntity source = sourceRepository.findById(sourceId)
                .orElseThrow(() -> new IllegalArgumentException("Knowledge source not found: " + sourceId));
        return source.getHealthStatus();
    }

    @Override
    @Transactional(readOnly = true)
    public List<KnowledgeSourceHealthEntity> getHealthHistory(String sourceId) {
        return healthRepository.findBySourceIdOrderByCheckedAtDesc(sourceId);
    }
}
