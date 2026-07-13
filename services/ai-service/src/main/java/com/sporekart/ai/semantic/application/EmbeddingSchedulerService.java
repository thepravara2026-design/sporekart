package com.sporekart.ai.semantic.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class EmbeddingSchedulerService {

    private static final Logger log = LoggerFactory.getLogger(EmbeddingSchedulerService.class);

    private final SemanticIndexService indexService;
    private final SemanticEmbeddingService embeddingService;

    public EmbeddingSchedulerService(SemanticIndexService indexService,
                                     SemanticEmbeddingService embeddingService) {
        this.indexService = indexService;
        this.embeddingService = embeddingService;
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void scheduledIndexRebuild() {
        log.info("Starting scheduled index rebuild");
        var indexes = indexService.listIndexes();
        for (var index : indexes) {
            try {
                indexService.rebuildIndex(index.getName());
                log.info("Scheduled rebuild for index: {}", index.getName());
            } catch (Exception e) {
                log.warn("Failed to schedule rebuild for index {}: {}", index.getName(), e.getMessage());
            }
        }
    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void scheduledCleanup() {
        log.info("Starting scheduled cleanup of deprecated embeddings");
        var allEmbeddings = embeddingService.listEmbeddings();
        int deprecatedCount = 0;
        for (var emb : allEmbeddings) {
            if ("DEPRECATED".equals(emb.getStatus()) && emb.getUpdatedAt() != null
                    && emb.getUpdatedAt().isBefore(OffsetDateTime.now().minus(90, ChronoUnit.DAYS))) {
                emb.setDeleted(true);
                emb.setDeletedAt(OffsetDateTime.now());
                deprecatedCount++;
            }
        }
        log.info("Scheduled cleanup completed: deprecated {} old embeddings", deprecatedCount);
    }

    @Scheduled(fixedRate = 3600000)
    public void scheduledHealthCheck() {
        log.debug("Semantic health check completed: indexes={}, embeddings={}",
                indexService.listIndexes().size(), embeddingService.listEmbeddings().size());
    }
}
