package com.sporekart.memory.summarization;

import com.sporekart.memory.domain.MemorySummaryType;
import com.sporekart.memory.persistence.MemoryEntity;
import com.sporekart.memory.persistence.MemorySummaryEntity;
import com.sporekart.memory.repository.MemoryRepository;
import com.sporekart.memory.repository.MemorySummaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SummarizationService {

    private static final Logger log = LoggerFactory.getLogger(SummarizationService.class);

    private final MemoryRepository memoryRepository;
    private final MemorySummaryRepository summaryRepository;

    public SummarizationService(MemoryRepository memoryRepository,
                                MemorySummaryRepository summaryRepository) {
        this.memoryRepository = memoryRepository;
        this.summaryRepository = summaryRepository;
    }

    @Transactional
    public SummaryResult summarize(MemoryEntity memory) {
        var currentOpt = summaryRepository.findTopByMemoryIdOrderByVersionDesc(memory.getId());
        int nextVersion = currentOpt.map(s -> s.getVersion() + 1).orElse(1);

        var summaryText = extractiveSummarize(memory.getContent(), memory.getTitle());

        var entity = new MemorySummaryEntity();
        entity.setMemoryId(memory.getId());
        entity.setSummaryText(summaryText);
        entity.setSummaryType(MemorySummaryType.AUTO_GENERATED.name());
        entity.setVersion(nextVersion);
        entity.setCreatedAt(OffsetDateTime.now());

        var saved = summaryRepository.save(entity);
        log.info("Generated summary version {} for memory {}", nextVersion, memory.getId());

        return new SummaryResult(saved.getId(), saved.getMemoryId(), saved.getSummaryText(),
                MemorySummaryType.valueOf(saved.getSummaryType()), saved.getVersion(), saved.getCreatedAt());
    }

    @Transactional
    public SummaryResult summarize(MemoryEntity memory, String manualSummary) {
        var currentOpt = summaryRepository.findTopByMemoryIdOrderByVersionDesc(memory.getId());
        int nextVersion = currentOpt.map(s -> s.getVersion() + 1).orElse(1);

        var entity = new MemorySummaryEntity();
        entity.setMemoryId(memory.getId());
        entity.setSummaryText(manualSummary);
        entity.setSummaryType(MemorySummaryType.MANUAL.name());
        entity.setVersion(nextVersion);
        entity.setCreatedAt(OffsetDateTime.now());

        var saved = summaryRepository.save(entity);
        log.info("Saved manual summary version {} for memory {}", nextVersion, memory.getId());

        return new SummaryResult(saved.getId(), saved.getMemoryId(), saved.getSummaryText(),
                MemorySummaryType.valueOf(saved.getSummaryType()), saved.getVersion(), saved.getCreatedAt());
    }

    public List<MemorySummaryEntity> getSummaries(UUID memoryId) {
        return summaryRepository.findByMemoryIdOrderByCreatedAtDesc(memoryId);
    }

    private String extractiveSummarize(String content, String title) {
        if (content == null || content.isBlank()) return "";
        var words = content.split("\\s+");
        if (words.length <= 100) return content;
        var sb = new StringBuilder();
        sb.append(title).append(": ");
        int firstSentences = Math.min(words.length, 200);
        for (int i = 0; i < firstSentences; i++) {
            sb.append(words[i]).append(" ");
        }
        sb.append("[... truncated]");
        return sb.toString().trim();
    }
}
