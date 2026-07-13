package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.domain.*;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalWorkflowEntity;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalWorkflowRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApprovalWorkflowServiceImplTest {

    @Mock
    private ApprovalWorkflowRepository workflowRepository;

    private ObjectMapper objectMapper;
    private ApprovalWorkflowServiceImpl workflowService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        workflowService = new ApprovalWorkflowServiceImpl(workflowRepository, objectMapper);
    }

    private ApprovalWorkflow createWorkflow() {
        return new ApprovalWorkflow(UUID.randomUUID(), "test", "desc", "content",
            List.of(ApprovalStatus.APPROVED, ApprovalStatus.REJECTED), 3, true, true,
            AssignmentStrategy.ROLE_BASED, 60, Map.of(), true,
            OffsetDateTime.now(), OffsetDateTime.now());
    }

    @Test
    void shouldCreateWorkflow() {
        var workflow = createWorkflow();
        when(workflowRepository.save(any())).thenAnswer(inv -> {
            var entity = inv.<ApprovalWorkflowEntity>getArgument(0);
            return entity;
        });

        var result = workflowService.createWorkflow(workflow);

        assertNotNull(result);
        assertEquals(workflow.name(), result.name());
        verify(workflowRepository).save(any());
    }

    @Test
    void shouldUpdateWorkflow() {
        var workflow = createWorkflow();
        var entity = new ApprovalWorkflowEntity();
        entity.setId(workflow.id());
        entity.setIsDeleted(false);

        when(workflowRepository.findByIdAndIsDeletedFalse(workflow.id())).thenReturn(Optional.of(entity));
        when(workflowRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = workflowService.updateWorkflow(workflow);

        assertEquals(workflow.name(), result.name());
    }

    @Test
    void shouldDeleteWorkflow() {
        var id = UUID.randomUUID();
        var entity = new ApprovalWorkflowEntity();
        entity.setId(id);
        entity.setIsDeleted(false);

        when(workflowRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));
        when(workflowRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        workflowService.deleteWorkflow(id);

        assertTrue(entity.getIsDeleted());
    }

    @Test
    void shouldGetWorkflow() {
        var id = UUID.randomUUID();
        var entity = new ApprovalWorkflowEntity();
        entity.setId(id);
        entity.setName("test");
        entity.setDescription("desc");
        entity.setModule("content");
        entity.setMaxLevels(3);
        entity.setParallelEnabled(true);
        entity.setSequentialEnabled(true);
        entity.setStrategy(AssignmentStrategy.ROLE_BASED.name());
        entity.setSlaMinutes(60);
        entity.setIsActive(true);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);

        when(workflowRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));

        var result = workflowService.getWorkflow(id);

        assertTrue(result.isPresent());
        assertEquals("test", result.get().name());
    }

    @Test
    void shouldAllowValidTransition() {
        var id = UUID.randomUUID();
        var entity = new ApprovalWorkflowEntity();
        entity.setId(id);
        entity.setAllowedTransitions("[\"APPROVED\",\"REJECTED\"]");
        entity.setIsDeleted(false);

        when(workflowRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));

        assertTrue(workflowService.canTransition(ApprovalStatus.PENDING, ApprovalStatus.APPROVED, id));
        assertTrue(workflowService.canTransition(ApprovalStatus.PENDING, ApprovalStatus.REJECTED, id));
    }

    @Test
    void shouldNotAllowInvalidTransition() {
        var id = UUID.randomUUID();
        var entity = new ApprovalWorkflowEntity();
        entity.setId(id);
        entity.setAllowedTransitions("[\"APPROVED\"]");
        entity.setIsDeleted(false);

        when(workflowRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));

        assertFalse(workflowService.canTransition(ApprovalStatus.PENDING, ApprovalStatus.CANCELLED, id));
    }
}
