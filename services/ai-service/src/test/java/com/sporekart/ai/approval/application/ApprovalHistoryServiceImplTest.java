package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.domain.*;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalCommentEntity;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalCommentRepository;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalHistoryEntity;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalHistoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApprovalHistoryServiceImplTest {

    @Mock
    private ApprovalHistoryRepository historyRepository;

    @Mock
    private ApprovalCommentRepository commentRepository;

    private ObjectMapper objectMapper;
    private ApprovalHistoryServiceImpl historyService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        historyService = new ApprovalHistoryServiceImpl(historyRepository, commentRepository, objectMapper);
    }

    @Test
    void shouldRecordHistory() {
        var history = new ApprovalHistory(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
            ApprovalDecision.APPROVE, "approved", Map.of(), OffsetDateTime.now());

        when(historyRepository.save(any())).thenAnswer(inv -> {
            var entity = inv.<ApprovalHistoryEntity>getArgument(0);
            return entity;
        });

        var result = historyService.recordHistory(history);

        assertNotNull(result);
        assertEquals(history.comment(), result.comment());
        verify(historyRepository).save(any());
    }

    @Test
    void shouldGetHistory() {
        var requestId = UUID.randomUUID();
        var entity = new ApprovalHistoryEntity();
        entity.setId(UUID.randomUUID());
        entity.setRequestId(requestId);
        entity.setDecision(ApprovalDecision.APPROVE.name());
        entity.setComment("approved");
        entity.setTimestamp(OffsetDateTime.now());
        entity.setIsDeleted(false);

        when(historyRepository.findByRequestIdAndIsDeletedFalse(requestId)).thenReturn(List.of(entity));

        var results = historyService.getHistory(requestId);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }

    @Test
    void shouldAddComment() {
        var comment = new ApprovalComment(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
            "Looks good", "review", OffsetDateTime.now());

        when(commentRepository.save(any())).thenAnswer(inv -> {
            var entity = inv.<ApprovalCommentEntity>getArgument(0);
            return entity;
        });

        var result = historyService.addComment(comment);

        assertNotNull(result);
        assertEquals(comment.comment(), result.comment());
        verify(commentRepository).save(any());
    }

    @Test
    void shouldGetComments() {
        var requestId = UUID.randomUUID();
        var entity = new ApprovalCommentEntity();
        entity.setId(UUID.randomUUID());
        entity.setRequestId(requestId);
        entity.setComment("Looks good");
        entity.setType("review");
        entity.setTimestamp(OffsetDateTime.now());
        entity.setIsDeleted(false);

        when(commentRepository.findByRequestIdAndIsDeletedFalse(requestId)).thenReturn(List.of(entity));

        var results = historyService.getComments(requestId);

        assertFalse(results.isEmpty());
        assertEquals("Looks good", results.get(0).comment());
    }
}
