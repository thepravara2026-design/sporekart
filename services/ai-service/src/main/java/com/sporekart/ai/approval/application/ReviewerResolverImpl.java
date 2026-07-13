package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.api.ReviewerResolver;
import com.sporekart.ai.approval.domain.*;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalReviewerEntity;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalReviewerRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewerResolverImpl implements ReviewerResolver {

    private final ApprovalReviewerRepository reviewerRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<ApprovalReviewer> resolveReviewers(ApprovalRequest request) {
        if (request.roles() != null && !request.roles().isEmpty()) {
            return resolveByRole(request.roles().get(0));
        }
        return resolveByDepartment(request.userId());
    }

    @Override
    public List<ApprovalReviewer> resolveByRole(String role) {
        return reviewerRepository.findByIsAvailableTrue().stream()
            .filter(e -> e.getRoles() != null && e.getRoles().contains(role))
            .map(this::toDomain).toList();
    }

    @Override
    public List<ApprovalReviewer> resolveByDepartment(String department) {
        return reviewerRepository.findByDepartmentAndIsDeletedFalse(department).stream()
            .map(this::toDomain).toList();
    }

    @Override
    public List<ApprovalReviewer> resolveByGroup(UUID groupId) {
        return reviewerRepository.findByIsAvailableTrue().stream()
            .limit(5)
            .map(this::toDomain).toList();
    }

    @Override
    public Optional<ApprovalReviewer> resolveFallback(ApprovalRequest request) {
        return reviewerRepository.findByIsAvailableTrue().stream()
            .findFirst()
            .map(this::toDomain);
    }

    @Override
    public Optional<ApprovalReviewer> resolveEmergency(ApprovalRequest request) {
        return reviewerRepository.findByIsAvailableTrue().stream()
            .max(Comparator.comparingInt(e -> e.getPriority() != null ? e.getPriority() : 0))
            .map(this::toDomain);
    }

    @Override
    public ApprovalReviewer selectReviewer(List<ApprovalReviewer> candidates, AssignmentStrategy strategy) {
        if (candidates == null || candidates.isEmpty()) {
            throw new RuntimeException("No candidates available");
        }
        return switch (strategy) {
            case ROUND_ROBIN -> candidates.stream()
                .min(Comparator.comparing(ApprovalReviewer::lastAssigned,
                    Comparator.nullsFirst(Comparator.naturalOrder())))
                .orElse(candidates.get(0));
            case PRIORITY -> candidates.stream()
                .max(Comparator.comparingInt(ApprovalReviewer::priority))
                .orElse(candidates.get(0));
            default -> candidates.get(0);
        };
    }

    private ApprovalReviewer toDomain(ApprovalReviewerEntity entity) {
        return new ApprovalReviewer(
            entity.getId(), entity.getUserId(), entity.getName(), entity.getEmail(),
            entity.getDepartment(),
            fromJson(entity.getRoles(), new TypeReference<List<String>>() {}),
            entity.getType() != null ? ReviewerType.valueOf(entity.getType()) : null,
            entity.getPriority() != null ? entity.getPriority() : 0,
            entity.getMaxAssignments() != null ? entity.getMaxAssignments() : 0,
            entity.getCurrentAssignments() != null ? entity.getCurrentAssignments() : 0,
            entity.getIsAvailable() != null && entity.getIsAvailable(),
            entity.getLastAssigned()
        );
    }

    private <T> T fromJson(String json, TypeReference<T> type) {
        try {
            return json == null ? null : objectMapper.readValue(json, type);
        } catch (Exception e) {
            return null;
        }
    }
}
