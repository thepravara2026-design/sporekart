package com.sporekart.memory.consolidation;

import com.sporekart.memory.domain.MemoryType;
import com.sporekart.memory.persistence.MemoryEntity;
import com.sporekart.memory.repository.MemoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConsolidationService {

    private static final Logger log = LoggerFactory.getLogger(ConsolidationService.class);

    private final MemoryRepository memoryRepository;

    public ConsolidationService(MemoryRepository memoryRepository) {
        this.memoryRepository = memoryRepository;
    }

    @Transactional
    public ConsolidationResult mergeByTopic(String topic, String workspace) {
        var candidates = memoryRepository.findByWorkspaceAndIsDeletedFalse(workspace).stream()
                .filter(m -> m.getContent() != null && m.getContent().toLowerCase().contains(topic.toLowerCase()))
                .toList();

        if (candidates.isEmpty()) {
            log.warn("No memory candidates found for topic '{}' in workspace '{}'", topic, workspace);
            return null;
        }

        var mergedTitle = "Consolidated: " + topic;
        var mergedContent = candidates.stream()
                .map(MemoryEntity::getContent)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n\n---\n\n"));

        var avgImportance = candidates.stream()
                .mapToInt(MemoryEntity::getImportance)
                .average()
                .orElse(0);

        var consolidated = new MemoryEntity();
        consolidated.setTitle(mergedTitle);
        consolidated.setContent(mergedContent);
        consolidated.setMemoryType(MemoryType.CONSOLIDATED);
        consolidated.setWorkspace(workspace);
        consolidated.setImportance((int) Math.round(avgImportance));
        consolidated.setCreatedAt(OffsetDateTime.now());
        consolidated.setIsDeleted(false);

        var saved = memoryRepository.save(consolidated);
        log.info("Consolidated {} memories into {}", candidates.size(), saved.getId());

        return new ConsolidationResult(saved.getId(), candidates.size(),
                mergedContent.substring(0, Math.min(200, mergedContent.length())),
                ConsolidationStrategy.MERGE, saved.getCreatedAt());
    }
}
