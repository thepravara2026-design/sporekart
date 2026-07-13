package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.domain.*;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalReviewerEntity;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalReviewerRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewerResolverImplTest {

    @Mock
    private ApprovalReviewerRepository reviewerRepository;

    private ObjectMapper objectMapper;
    private ReviewerResolverImpl resolver;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        resolver = new ReviewerResolverImpl(reviewerRepository, objectMapper);
    }

    private ApprovalReviewerEntity createReviewerEntity(String role) {
        var entity = new ApprovalReviewerEntity();
        entity.setId(UUID.randomUUID());
        entity.setUserId("reviewer1");
        entity.setName("Reviewer One");
        entity.setEmail("r1@test.com");
        entity.setDepartment("dept1");
        entity.setRoles("[\"" + role + "\"]");
        entity.setType(ReviewerType.ROLE_BASED.name());
        entity.setPriority(5);
        entity.setMaxAssignments(10);
        entity.setCurrentAssignments(2);
        entity.setIsAvailable(true);
        entity.setLastAssigned(OffsetDateTime.now().minusDays(1));
        return entity;
    }

    @Test
    void shouldResolveReviewersByRole() {
        var entity = createReviewerEntity("admin");
        when(reviewerRepository.findByIsAvailableTrue()).thenReturn(List.of(entity));

        var request = new ApprovalRequest(UUID.randomUUID(), "content", "publish",
            Map.of(), Map.of(), "user1", List.of("admin"),
            "reason", "normal", null, Map.of(), null, ApprovalStatus.PENDING, OffsetDateTime.now());

        var result = resolver.resolveReviewers(request);

        assertFalse(result.isEmpty());
        assertEquals("Reviewer One", result.get(0).name());
    }

    @Test
    void shouldResolveByRole() {
        var entity = createReviewerEntity("admin");
        when(reviewerRepository.findByIsAvailableTrue()).thenReturn(List.of(entity));

        var result = resolver.resolveByRole("admin");

        assertFalse(result.isEmpty());
        assertEquals("reviewer1", result.get(0).userId());
    }

    @Test
    void shouldResolveFallback() {
        var entity = createReviewerEntity("admin");
        when(reviewerRepository.findByIsAvailableTrue()).thenReturn(List.of(entity));

        var request = new ApprovalRequest(UUID.randomUUID(), "content", "publish",
            Map.of(), Map.of(), "user1", List.of(),
            "reason", "normal", null, Map.of(), null, ApprovalStatus.PENDING, OffsetDateTime.now());

        var result = resolver.resolveFallback(request);

        assertTrue(result.isPresent());
        assertEquals("reviewer1", result.get().userId());
    }

    @Test
    void shouldReturnEmptyWhenNoReviewersAvailable() {
        when(reviewerRepository.findByIsAvailableTrue()).thenReturn(List.of());

        var request = new ApprovalRequest(UUID.randomUUID(), "content", "publish",
            Map.of(), Map.of(), "user1", List.of(),
            "reason", "normal", null, Map.of(), null, ApprovalStatus.PENDING, OffsetDateTime.now());

        var result = resolver.resolveFallback(request);

        assertTrue(result.isEmpty());
    }
}
