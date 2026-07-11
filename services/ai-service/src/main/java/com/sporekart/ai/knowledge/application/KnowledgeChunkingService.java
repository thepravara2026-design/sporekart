package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.infrastructure.KnowledgeKafkaEventPublisher;
import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeChunkEntity;
import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeChunkRepository;
import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeDocumentEntity;
import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeDocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class KnowledgeChunkingService {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeChunkingService.class);
    private static final int DEFAULT_CHUNK_SIZE = 1000;
    private static final int DEFAULT_OVERLAP = 100;
    private static final int MAX_CHUNK_SIZE = 5000;

    private final KnowledgeChunkRepository chunkRepository;
    private final KnowledgeDocumentRepository documentRepository;
    private final KnowledgeKafkaEventPublisher kafkaPublisher;

    public KnowledgeChunkingService(KnowledgeChunkRepository chunkRepository,
                                    KnowledgeDocumentRepository documentRepository,
                                    KnowledgeKafkaEventPublisher kafkaPublisher) {
        this.chunkRepository = chunkRepository;
        this.documentRepository = documentRepository;
        this.kafkaPublisher = kafkaPublisher;
    }

    public List<KnowledgeChunkEntity> getChunks(UUID documentId) {
        return chunkRepository.findByDocumentIdAndIsDeletedFalseOrderByChunkIndex(documentId);
    }

    @Transactional
    public List<KnowledgeChunkEntity> chunkDocument(UUID documentId, int chunkSize, int overlap) {
        KnowledgeDocumentEntity doc = documentRepository.findByIdAndIsDeletedFalse(documentId)
                .orElseThrow(() -> new KnowledgeNotFoundException("Document not found: " + documentId));

        if (doc.getContent() == null || doc.getContent().isBlank()) {
            throw new KnowledgeValidationException("Document has no content to chunk");
        }

        chunkRepository.deleteByDocumentId(documentId);

        int effectiveSize = Math.min(chunkSize > 0 ? chunkSize : DEFAULT_CHUNK_SIZE, MAX_CHUNK_SIZE);
        int effectiveOverlap = Math.min(overlap >= 0 ? overlap : DEFAULT_OVERLAP, effectiveSize / 2);
        String content = doc.getContent();
        List<KnowledgeChunkEntity> chunks = new ArrayList<>();

        int start = 0;
        int index = 0;
        while (start < content.length()) {
            int end = Math.min(start + effectiveSize, content.length());
            if (end < content.length() && end - start == effectiveSize) {
                int lastSpace = content.lastIndexOf(' ', end);
                if (lastSpace > start) end = lastSpace;
            }
            String chunkText = content.substring(start, end).trim();
            if (!chunkText.isEmpty()) {
                KnowledgeChunkEntity chunk = new KnowledgeChunkEntity(doc, index, chunkText);
                chunk.setCharCount(chunkText.length());
                chunk.setTokenCount(estimateTokens(chunkText));
                chunk.setChunkSizeStrategy("FIXED");
                chunk = chunkRepository.save(chunk);
                chunks.add(chunk);
                kafkaPublisher.publishChunkCreated(documentId, index);
                index++;
            }
            int nextStart = end - effectiveOverlap;
            if (nextStart <= start) nextStart = end;
            start = nextStart;
        }

        log.info("Chunked document {} into {} chunks", documentId, chunks.size());
        kafkaPublisher.publishIndexed(documentId, chunks.size());
        return chunks;
    }

    @Transactional
    public void validateChunks(UUID documentId) {
        List<KnowledgeChunkEntity> chunks = chunkRepository.findByDocumentIdAndIsDeletedFalseOrderByChunkIndex(documentId);
        if (chunks.isEmpty()) {
            throw new KnowledgeValidationException("No chunks found for document: " + documentId);
        }
        for (KnowledgeChunkEntity chunk : chunks) {
            if (chunk.getContent() == null || chunk.getContent().isBlank()) {
                throw new KnowledgeValidationException("Empty content in chunk " + chunk.getChunkIndex());
            }
            if (chunk.getCharCount() == 0) {
                throw new KnowledgeValidationException("Zero character count in chunk " + chunk.getChunkIndex());
            }
        }
    }

    private int estimateTokens(String text) {
        if (text == null || text.isEmpty()) return 0;
        return (int) Math.ceil(text.length() / 4.0);
    }
}
