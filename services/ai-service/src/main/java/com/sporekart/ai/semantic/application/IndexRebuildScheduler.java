package com.sporekart.ai.semantic.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class IndexRebuildScheduler {

    private static final Logger log = LoggerFactory.getLogger(IndexRebuildScheduler.class);

    private final SemanticIndexService indexService;
    private final AtomicInteger rebuildCount = new AtomicInteger(0);

    public IndexRebuildScheduler(SemanticIndexService indexService) {
        this.indexService = indexService;
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void scheduledRebuild() {
        log.info("Starting scheduled index rebuild");
        var indexes = indexService.listIndexes();
        int count = 0;
        for (var index : indexes) {
            try {
                indexService.rebuildIndex(index.getName());
                count++;
                rebuildCount.incrementAndGet();
                log.info("Scheduled rebuild for index: {}", index.getName());
            } catch (Exception e) {
                log.warn("Failed to rebuild index {}: {}", index.getName(), e.getMessage());
            }
        }
        log.info("Scheduled rebuild completed: {} indexes rebuilt", count);
    }

    @Scheduled(cron = "0 0 5 * * ?")
    public void scheduledOptimize() {
        log.info("Starting scheduled index optimization");
        var indexes = indexService.listIndexes();
        for (var index : indexes) {
            try {
                indexService.optimizeIndex(index.getName());
                log.info("Scheduled optimize for index: {}", index.getName());
            } catch (Exception e) {
                log.warn("Failed to optimize index {}: {}", index.getName(), e.getMessage());
            }
        }
    }

    @Scheduled(fixedRate = 3_600_000)
    public void logIndexStats() {
        var indexes = indexService.listIndexes();
        for (var index : indexes) {
            log.info("Index stats: name={}, status={}, vectors={}, dimensions={}",
                    index.getName(), index.getStatus(), index.getVectorCount(), index.getDimensions());
        }
    }

    public int getTotalRebuildCount() {
        return rebuildCount.get();
    }

    public void triggerImmediateRebuild(String indexName) {
        indexService.rebuildIndex(indexName);
        rebuildCount.incrementAndGet();
        log.info("Immediate rebuild triggered for index: {}", indexName);
    }
}
