package com.sporekart.ai.approval.interfaces.rest;

import com.sporekart.ai.approval.api.*;
import com.sporekart.ai.approval.domain.*;
import com.sporekart.ai.approval.infrastructure.kafka.ApprovalKafkaEventPublisher;
import com.sporekart.ai.approval.infrastructure.monitoring.ApprovalMonitoringService;
import com.sporekart.ai.approval.interfaces.rest.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalEngine engine;
    private final ApprovalMetricsService metricsService;
    private final ApprovalHistoryService historyService;
    private final ApprovalKafkaEventPublisher eventPublisher;
    private final ApprovalMonitoringService monitoringService;

    @PostMapping
    public ResponseEntity<ApprovalResponseDto> submit(@RequestBody ApprovalRequestDto request) {
        ApprovalRequest domain = new ApprovalRequest(
            UUID.randomUUID(),
            request.module(),
            request.action(),
            request.payload() != null ? request.payload() : new HashMap<>(),
            request.context() != null ? request.context() : new HashMap<>(),
            request.userId(),
            request.roles() != null ? request.roles() : List.of(),
            request.reason(),
            request.urgency(),
            null,
            new HashMap<>(),
            null,
            ApprovalStatus.PENDING,
            OffsetDateTime.now()
        );
        ApprovalRequest result = engine.submit(domain);
        metricsService.recordSubmission();
        eventPublisher.publishCreated(result.id().toString(), result.module());
        monitoringService.recordRequest();
        return ResponseEntity.created(URI.create("/api/v1/approvals/" + result.id()))
            .body(toApprovalResponse(result));
    }

    @GetMapping
    public ResponseEntity<ApprovalListDto> list() {
        return ResponseEntity.ok(new ApprovalListDto(List.of(), 0));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApprovalResponseDto> getById(@PathVariable UUID id) {
        ApprovalRequest approval = engine.getStatus(id);
        if (approval == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toApprovalResponse(approval));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApprovalResponseDto> approve(
            @PathVariable UUID id,
            @RequestBody ApprovalActionDto action) {
        long start = System.currentTimeMillis();
        ApprovalRequest result = engine.approve(id, action.reviewerId(), action.comment());
        long elapsed = System.currentTimeMillis() - start;
        metricsService.recordApproval(elapsed);
        eventPublisher.publishApproved(id.toString(), action.reviewerId());
        monitoringService.recordAction("APPROVED");
        return ResponseEntity.ok(toApprovalResponse(result));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApprovalResponseDto> reject(
            @PathVariable UUID id,
            @RequestBody ApprovalActionDto action) {
        long start = System.currentTimeMillis();
        ApprovalRequest result = engine.reject(id, action.reviewerId(), action.comment());
        long elapsed = System.currentTimeMillis() - start;
        metricsService.recordRejection(elapsed);
        eventPublisher.publishRejected(id.toString(), action.reviewerId());
        monitoringService.recordAction("REJECTED");
        return ResponseEntity.ok(toApprovalResponse(result));
    }

    @PostMapping("/{id}/delegate")
    public ResponseEntity<ApprovalResponseDto> delegate(
            @PathVariable UUID id,
            @RequestBody DelegateDto delegate) {
        UUID fromId = UUID.fromString(delegate.fromReviewerId());
        UUID toId = UUID.fromString(delegate.toReviewerId());
        ApprovalRequest result = engine.delegate(id, fromId, toId, delegate.reason());
        metricsService.recordDelegation();
        eventPublisher.publishDelegated(id.toString(), delegate.fromReviewerId(), delegate.toReviewerId());
        monitoringService.recordAction("DELEGATED");
        return ResponseEntity.ok(toApprovalResponse(result));
    }

    @PostMapping("/{id}/escalate")
    public ResponseEntity<ApprovalResponseDto> escalate(
            @PathVariable UUID id,
            @RequestBody EscalateDto escalate) {
        EscalationReason reason = EscalationReason.valueOf(escalate.reason().toUpperCase());
        UUID reviewerId = UUID.fromString(escalate.reviewerId());
        ApprovalRequest result = engine.escalate(id, reviewerId, reason, escalate.details());
        metricsService.recordEscalation();
        eventPublisher.publishEscalated(id.toString(), escalate.reviewerId(), escalate.details());
        monitoringService.recordAction("ESCALATED");
        return ResponseEntity.ok(toApprovalResponse(result));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApprovalResponseDto> cancel(
            @PathVariable UUID id,
            @RequestBody CancelDto cancel) {
        ApprovalRequest result = engine.cancel(id, cancel.userId(), cancel.reason());
        eventPublisher.publishCancelled(id.toString(), cancel.userId());
        monitoringService.recordAction("CANCELLED");
        return ResponseEntity.ok(toApprovalResponse(result));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApprovalListDto> pending(@RequestParam String userId) {
        List<ApprovalRequest> pendingList = engine.getPendingApprovals(userId);
        List<ApprovalResponseDto> dtos = pendingList.stream().map(this::toApprovalResponse).toList();
        return ResponseEntity.ok(new ApprovalListDto(dtos, dtos.size()));
    }

    @GetMapping("/history")
    public ResponseEntity<ApprovalHistoryDto> history(@RequestParam(required = false) UUID requestId) {
        List<ApprovalHistory> entries;
        if (requestId != null) {
            entries = engine.getHistory(requestId);
        } else {
            entries = historyService.getHistory(requestId != null ? requestId : UUID.randomUUID());
        }
        List<HistoryEntryDto> dtos = entries.stream()
            .map(h -> new HistoryEntryDto(
                h.id().toString(),
                h.decision().name(),
                h.comment(),
                h.reviewerId() != null ? h.reviewerId().toString() : null,
                h.timestamp() != null ? h.timestamp().toString() : null
            ))
            .toList();
        return ResponseEntity.ok(new ApprovalHistoryDto(dtos, dtos.size()));
    }

    @GetMapping("/statistics")
    public ResponseEntity<ApprovalStatsDto> statistics() {
        return ResponseEntity.ok(new ApprovalStatsDto(
            metricsService.getTotalRequests(),
            metricsService.getPendingCount(),
            metricsService.getApprovedCount(),
            metricsService.getRejectedCount(),
            metricsService.getAverageReviewTimeMs(),
            metricsService.getStatistics()
        ));
    }

    @GetMapping("/health")
    public ResponseEntity<HealthDto> health() {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("totalRequests", metricsService.getTotalRequests());
        details.put("pending", metricsService.getPendingCount());
        return ResponseEntity.ok(new HealthDto(
            "UP", "approval-service", System.currentTimeMillis(), details
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorDto.of(500, "Internal Server Error", ex.getMessage()));
    }

    private ApprovalResponseDto toApprovalResponse(ApprovalRequest r) {
        return new ApprovalResponseDto(
            r.id().toString(),
            r.module(),
            r.action(),
            r.status().name(),
            r.userId(),
            r.reason(),
            r.urgency(),
            r.createdAt() != null ? r.createdAt().toInstant().toEpochMilli() : 0L
        );
    }
}
