package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.api.ApprovalHistoryService;
import com.sporekart.ai.approval.domain.*;
import com.sporekart.ai.approval.infrastructure.persistence.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApprovalHistoryServiceImpl implements ApprovalHistoryService {

    private final ApprovalHistoryRepository historyRepository;
    private final ApprovalCommentRepository commentRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public ApprovalHistory recordHistory(ApprovalHistory history) {
        var entity = new ApprovalHistoryEntity();
        entity.setId(history.id() != null ? history.id() : UUID.randomUUID());
        entity.setRequestId(history.requestId());
        entity.setReviewerId(history.reviewerId());
        entity.setDecision(history.decision() != null ? history.decision().name() : null);
        entity.setComment(history.comment());
        entity.setDetails(toJson(history.details()));
        entity.setTimestamp(history.timestamp() != null ? history.timestamp() : OffsetDateTime.now());
        entity.setIsDeleted(false);
        var saved = historyRepository.save(entity);
        log.debug("History recorded for request: {}", history.requestId());
        return toDomain(saved);
    }

    @Override
    public List<ApprovalHistory> getHistory(UUID requestId) {
        return historyRepository.findByRequestIdAndIsDeletedFalse(requestId).stream()
            .map(this::toDomain).toList();
    }

    @Override
    public List<ApprovalHistory> getHistoryByReviewer(UUID reviewerId) {
        return historyRepository.findByReviewerIdAndIsDeletedFalse(reviewerId).stream()
            .map(this::toDomain).toList();
    }

    @Override
    public List<ApprovalHistory> getHistoryByDateRange(OffsetDateTime start, OffsetDateTime end) {
        return historyRepository.findAll().stream()
            .filter(e -> e.getIsDeleted() == null || !e.getIsDeleted())
            .filter(e -> e.getTimestamp() != null
                && !e.getTimestamp().isBefore(start)
                && !e.getTimestamp().isAfter(end))
            .map(this::toDomain).toList();
    }

    @Override
    @Transactional
    public ApprovalComment addComment(ApprovalComment comment) {
        var entity = new ApprovalCommentEntity();
        entity.setId(comment.id() != null ? comment.id() : UUID.randomUUID());
        entity.setRequestId(comment.requestId());
        entity.setReviewerId(comment.reviewerId());
        entity.setComment(comment.comment());
        entity.setType(comment.type());
        entity.setTimestamp(comment.timestamp() != null ? comment.timestamp() : OffsetDateTime.now());
        entity.setIsDeleted(false);
        var saved = commentRepository.save(entity);
        log.debug("Comment added for request: {}", comment.requestId());
        return toDomain(saved);
    }

    @Override
    public List<ApprovalComment> getComments(UUID requestId) {
        return commentRepository.findByRequestIdAndIsDeletedFalse(requestId).stream()
            .map(this::toDomain).toList();
    }

    private ApprovalHistory toDomain(ApprovalHistoryEntity entity) {
        return new ApprovalHistory(
            entity.getId(), entity.getRequestId(), entity.getReviewerId(),
            entity.getDecision() != null ? ApprovalDecision.valueOf(entity.getDecision()) : null,
            entity.getComment(),
            fromJson(entity.getDetails(), new TypeReference<Map<String, Object>>() {}),
            entity.getTimestamp()
        );
    }

    private ApprovalComment toDomain(ApprovalCommentEntity entity) {
        return new ApprovalComment(
            entity.getId(), entity.getRequestId(), entity.getReviewerId(),
            entity.getComment(), entity.getType(), entity.getTimestamp()
        );
    }

    private String toJson(Object value) {
        try {
            return value == null ? null : objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }

    private <T> T fromJson(String json, TypeReference<T> type) {
        try {
            return json == null ? null : objectMapper.readValue(json, type);
        } catch (Exception e) {
            return null;
        }
    }
}
